// ISecurityCenter.aidl
package com.xht.androidnote.module.ipc.binderpool;

interface ISecurityCenter {
   String encrypt(String content);
   String decrypt(String password);
}
