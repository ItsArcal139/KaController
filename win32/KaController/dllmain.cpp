// dllmain.cpp : Defines the entry point for the DLL application.
#include "framework.h"

BOOL APIENTRY DllMain( HMODULE hModule,
                       DWORD  ul_reason_for_call,
                       LPVOID lpReserved
                     )
{
    switch (ul_reason_for_call)
    {
    case DLL_PROCESS_ATTACH:
    case DLL_THREAD_ATTACH:
    case DLL_THREAD_DETACH:
    case DLL_PROCESS_DETACH:
        break;
    }
    return TRUE;
}

typedef DWORD(WINAPI* XInputGetStatePointer)(DWORD dwUserIndex, XINPUT_STATE* pState);
typedef DWORD(WINAPI* XInputSetStatePointer)(DWORD dwUserIndex, XINPUT_VIBRATION* pVibration);
typedef DWORD(WINAPI* XInputGetBatteryInformationPointer)(DWORD dwUserIndex, BYTE devType, XINPUT_BATTERY_INFORMATION* pBatteryInformation);

class XInputLoader
{
public:
	XInputLoader()
		: mLoaded(false), mHandle(0), mGetState(0), mSetState(0), mGetBatteryInfo(0)
	{
	}

	~XInputLoader()
	{
	}

	void ensureLoaded()
	{
		if (!mLoaded)
		{
			// Keep hold of error codes from each library we try to load.
			DWORD xinput1_3_ErrorCode = 0;
			DWORD xinput1_4_ErrorCode = 0;
			DWORD xinput9_1_ErrorCode = 0;

			bool isXInput14 = true;

			// Try XInput 1.4 first as it has all the features we need.
			mHandle = LoadLibrary(L"xinput1_4.dll");
			xinput1_4_ErrorCode = GetLastError();

			// Look for XInput 1.3 as a backup 
			if (mHandle == NULL)
			{
				isXInput14 = false;
				mHandle = LoadLibrary(L"xinput1_3.dll");
				xinput1_3_ErrorCode = GetLastError();
			}

			// Look for XInput 9.1.0 as a last resort! One of the others should exist but we may as well try to load it.
			if (mHandle == NULL)
			{
				mHandle = LoadLibrary(L"xinput9_1_0.dll");
				xinput9_1_ErrorCode = GetLastError();
			}

			if (mHandle != NULL)
			{
				mGetState = (XInputGetStatePointer)GetProcAddress(mHandle, (LPCSTR)100); // Ordinal 100 is the same as XInputGetState but supports the Guide button.
				mSetState = (XInputSetStatePointer)GetProcAddress(mHandle, "XInputSetState");

				if (isXInput14) {
					mGetBatteryInfo = (XInputGetBatteryInformationPointer)GetProcAddress(mHandle, "XInputGetBatteryInformation");
				}
				
				mLoaded = true;
			}
			else
			{
				printf_s("[XInput.NET] Failed to load xinput1_4.dll, xinput1_3.dll and xinput9_1_0.dll (error codes 0x%08x, 0x%08x, 0x%08x respectively; check that \"DirectX End-User Runtimes (June 2010)\""
					" is installed (http://www.microsoft.com/en-us/download/details.aspx?id=8109)\n", xinput1_4_ErrorCode, xinput1_3_ErrorCode, xinput9_1_ErrorCode);
			}
		}
	}

	XInputGetStatePointer mGetState;
	XInputSetStatePointer mSetState;
	XInputGetBatteryInformationPointer mGetBatteryInfo;

private:
	bool mLoaded;
	HMODULE mHandle;
};

XInputLoader gXInputLoader;

// ----------------

#define K_JavaModNamespace "com/kakaouo/mods/kacontroller"
#define K_NativeHelperClzName K_JavaModNamespace "/utils/NativeHelper"
#define K_XInnerClass(x) K_JavaModNamespace "/utils/XInputNative$" x

class KPendingException {};


void KThrowNewAssertionError(JNIEnv* _env, const char* _msg) {
    jclass clz = _env->FindClass("java/lang/AssertionError");
    _env->ThrowNew(clz, _msg);
}

bool KIsObjectEnum(JNIEnv* _env, jobject _obj) {
	jclass clz = _env->FindClass(K_NativeHelperClzName);
	jmethodID mtIsEnum = _env->GetStaticMethodID(clz, "isEnum", "(Ljava/lang/Object;)Z");
	return _env->CallStaticBooleanMethod(clz, mtIsEnum, _obj);
}

int KGetEnumOrdinal(JNIEnv* _env, jobject _enum) {
	if (!KIsObjectEnum(_env, _enum)) {
		KThrowNewAssertionError(_env, "The given object is not an Enum.");
		return -1;
	}

	jclass clz = _env->GetObjectClass(_enum);
	jmethodID mt = _env->GetMethodID(clz, "ordinal", "()I");
	return _env->CallIntMethod(_enum, mt);
}

DWORD XInputGamePadGetState(DWORD dwUserIndex, XINPUT_STATE* pState)
{
	gXInputLoader.ensureLoaded();
	if (gXInputLoader.mGetState != NULL)
	{
		return gXInputLoader.mGetState(dwUserIndex, pState);
	}
	else
	{
		return ERROR_DEVICE_NOT_CONNECTED;
	}
}

void XInputGamePadSetState(DWORD dwUserIndex, float leftMotor, float rightMotor)
{
	gXInputLoader.ensureLoaded();
	if (gXInputLoader.mSetState != NULL)
	{
		leftMotor = leftMotor < 0.0f ? 0.0f : (leftMotor > 1.0f ? 1.0f : leftMotor);
		rightMotor = rightMotor < 0.0f ? 0.0f : (rightMotor > 1.0f ? 1.0f : rightMotor);
		XINPUT_VIBRATION vibration = { (WORD)(leftMotor * 65535), (WORD)(rightMotor * 65535) };
		gXInputLoader.mSetState(dwUserIndex, &vibration);
	}
}

DWORD XInputGamePadGetBatteryInfo(DWORD dwUserIndex, BYTE devType, XINPUT_BATTERY_INFORMATION* pBatteryInformation) {
	gXInputLoader.ensureLoaded();
	if (gXInputLoader.mGetState != NULL)
	{
		return gXInputLoader.mGetBatteryInfo(dwUserIndex, devType, pBatteryInformation);
	}
	else
	{
		return ERROR_DEVICE_NOT_CONNECTED;
	}
}

jobject KMakeGamePad(JNIEnv* _env, XINPUT_GAMEPAD* gamepad) {
	jclass clz = _env->FindClass(K_NativeHelperClzName);
	jmethodID mtCreateGamePad = _env->GetStaticMethodID(clz, "createGamePad", "(SBBSSSS)L" K_XInnerClass("RawGamePad") ";");
	return _env->CallStaticObjectMethod(clz, mtCreateGamePad,
		gamepad->wButtons, gamepad->bLeftTrigger, gamepad->bRightTrigger,
		gamepad->sThumbLX, gamepad->sThumbLY,
		gamepad->sThumbRX, gamepad->sThumbRY);
}

jobject KMakeGamePadState(JNIEnv* _env, XINPUT_STATE* _state) {
	jclass clz = _env->FindClass(K_NativeHelperClzName);
	jmethodID mtCreateGamePadState = _env->GetStaticMethodID(clz, "createGamePadState", "(IL" K_XInnerClass("RawGamePad") ";)L" K_XInnerClass("RawGamePadState") ";");

	XINPUT_GAMEPAD* gamepad = &_state->Gamepad;
	return _env->CallStaticObjectMethod(clz, mtCreateGamePadState, _state->dwPacketNumber, KMakeGamePad(_env, gamepad));
}

jobject KMakeStateResult(JNIEnv* _env, DWORD _result, XINPUT_STATE* _state) {
	jclass clz = _env->FindClass(K_NativeHelperClzName);
	jmethodID mtCreateStateResult = _env->GetStaticMethodID(clz, "createStateResult", "(IL" K_XInnerClass("RawGamePadState") ";)L" K_XInnerClass("StateResult") ";");
	return _env->CallStaticObjectMethod(clz, mtCreateStateResult, _result, KMakeGamePadState(_env, _state));
}

jobject KMakeBatteryInfoResult(JNIEnv* _env, DWORD _result, XINPUT_BATTERY_INFORMATION* _info) {
	jclass clz = _env->FindClass(K_NativeHelperClzName);
	jmethodID mtCreateStateResult = _env->GetStaticMethodID(clz, "createBatteryInfoResult", "(ISS)L" K_XInnerClass("BatteryInfoResult") ";");
	return _env->CallStaticObjectMethod(clz, mtCreateStateResult, _result, _info->BatteryType, _info->BatteryLevel);
}

// Exported JNI methods
extern "C" {
    JNIEXPORT jobject JNICALL Java_com_kakaouo_mods_kacontroller_utils_XInputNative_internalGetState
    (JNIEnv* _env, jclass _clz, jobject _playerIndex) {
        try {
            jint index = KGetEnumOrdinal(_env, _playerIndex);
            if (_env->ExceptionCheck()) {
                throw KPendingException();
            }

			XINPUT_STATE state;
			DWORD result = XInputGamePadGetState(index, &state);
			return KMakeStateResult(_env, result, &state);
        }
        catch (KPendingException &e) {
            // ;
        }
    }

	JNIEXPORT jobject JNICALL Java_com_kakaouo_mods_kacontroller_utils_XInputNative_internalGetBatteryInfo
	(JNIEnv* _env, jclass _clz , jobject _playerIndex) {
		try {
			jint index = KGetEnumOrdinal(_env, _playerIndex);
			if (_env->ExceptionCheck()) {
				throw KPendingException();
			}

			XINPUT_BATTERY_INFORMATION info;
			DWORD result = XInputGamePadGetBatteryInfo(index, BATTERY_DEVTYPE_GAMEPAD, &info);
			return KMakeBatteryInfoResult(_env, result, &info);
		}
		catch (KPendingException& e) {
			// ;
		}
	}

	JNIEXPORT void JNICALL Java_com_kakaouo_mods_kacontroller_utils_XInputNative_internalSetState
	(JNIEnv* _env, jclass _clz, jobject _playerIndex, jfloat _leftMotor, jfloat _rightMotor) {
		try {
			jint index = KGetEnumOrdinal(_env, _playerIndex);
			if (_env->ExceptionCheck()) {
				throw KPendingException();
			}

			XInputGamePadSetState(index, _leftMotor, _rightMotor);
		}
		catch (KPendingException& e) {
			// ;
		}
	}
}