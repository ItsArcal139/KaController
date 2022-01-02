#pragma once
#include <jni.h>

#ifndef _Included_com_kakaouo_mods_kacontroller_utils_XInputNative
#define _Included_com_kakaouo_mods_kacontroller_utils_XInputNative
#ifdef __cplusplus
extern "C" {
#endif
	/*
	 * Class:     com_kakaouo_mods_kacontroller_utils_XInputNative
	 * Method:    internalGetState
	 * Signature: (Lcom/kakaouo/mods/kacontroller/utils/PlayerIndex;)Lcom/kakaouo/mods/kacontroller/utils/XInputNative/StateResult;
	 */
	JNIEXPORT jobject JNICALL Java_com_kakaouo_mods_kacontroller_utils_XInputNative_internalGetState
	(JNIEnv*, jclass, jobject);

	/*
	 * Class:     com_kakaouo_mods_kacontroller_utils_XInputNative
	 * Method:    internalSetState
	 * Signature: (Lcom/kakaouo/mods/kacontroller/utils/PlayerIndex;FF)Lcom/kakaouo/mods/kacontroller/utils/XInputNative/StateResult;
	 */
	JNIEXPORT jobject JNICALL Java_com_kakaouo_mods_kacontroller_utils_XInputNative_internalSetState
	(JNIEnv*, jclass, jobject, jfloat, jfloat);

#ifdef __cplusplus
}
#endif
#endif