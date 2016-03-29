//
// Created by Administrator on 2016/3/29 0029.
//
#include<stdlib.h>
#include<stdio.h>
#include<jni.h>

jstring  Java_com_example_jni_MainActivity_getHello(JNIEnv * env ,jobject obj){

       char * pStr = "hello i am jni";

     // jstring  jStr = *(*env).NewStringUTF(env,pStr);

    jstring  jStr = (*env)->NewStringUTF(env,pStr);

    return  jStr;

}
