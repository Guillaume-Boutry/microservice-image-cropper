#include "org_boutry_wrapper_natimage_NatImage.h"
#include <iostream>
#include <png.h>
#include <stdint.h>

typedef struct _image {
    unsigned char* buffer;
    uint32_t size;
} Image;

unsigned char* as_unsigned_char_array(JNIEnv* env, jbyteArray buffer);
void* as_image_struct(JNIEnv* env, jbyteArray buffer, Image* image);
jbyteArray as_byte_array(JNIEnv* env, unsigned char* buffer, int length);


JNIEXPORT jboolean JNICALL Java_org_boutry_wrapper_natimage_NatImage_isPng(JNIEnv* env, jclass jNatImage, jbyteArray buffer){
    jboolean is_copy;
    unsigned char* array =  as_unsigned_char_array(env, buffer);
    int is_png = !png_sig_cmp(array, 0, 8);
    std::cout << "Is the image png: " << is_png << std::endl;
    free(array);
    return is_png;
  }

JNIEXPORT void JNICALL Java_org_boutry_wrapper_natimage_NatImage_initPng(JNIEnv* env, jobject jNatImage, jbyteArray buffer) {
    Image* image = (Image*) malloc(sizeof(Image));
    as_image_struct(env, buffer, image);
    jclass NatImageClass = env->GetObjectClass(jNatImage);
    jfieldID handle = env->GetFieldID(NatImageClass, "_handleImage", "J");
    env->SetLongField(jNatImage, handle, (jlong) image);
   }

JNIEXPORT void JNICALL Java_org_boutry_wrapper_natimage_NatImage_dispose(JNIEnv* env, jobject jNatImage){
    jclass NatImageClass = env->GetObjectClass(jNatImage);
    jfieldID handle = env->GetFieldID(NatImageClass, "_handleImage", "J");
    Image* image = (Image*) env->GetLongField(jNatImage, handle);
    free(image->buffer);
    free(image);
  }

JNIEXPORT jbyteArray JNICALL Java_org_boutry_wrapper_natimage_NatImage_getImage(JNIEnv* env, jobject jNatImage){
    jclass NatImageClass = env->GetObjectClass(jNatImage);
    jfieldID handle = env->GetFieldID(NatImageClass, "_handleImage", "J");
    Image* image = (Image*) env->GetLongField(jNatImage, handle);
    jbyteArray buffer = as_byte_array(env, image->buffer, image->size);
    return buffer;
}

unsigned char* as_unsigned_char_array(JNIEnv* env, jbyteArray buffer) {
    int len = env->GetArrayLength(buffer);
    unsigned char* array = new unsigned char[len];
    env->GetByteArrayRegion(buffer, 0, len, reinterpret_cast<jbyte*>(array));
    return array;
}

void* as_image_struct(JNIEnv* env, jbyteArray buffer, Image* image){
    uint32_t len = (uint32_t) env->GetArrayLength(buffer);
    unsigned char* array = new unsigned char[len];
    env->GetByteArrayRegion(buffer, 0, len, reinterpret_cast<jbyte*>(array));
    image->buffer = array;
    image->size = len;
}

jbyteArray as_byte_array(JNIEnv* env, unsigned char* buffer, int length) {
    jbyteArray array = env->NewByteArray(length);
    env->SetByteArrayRegion(array, 0, length, reinterpret_cast<jbyte*>(buffer));
    return array;
}
