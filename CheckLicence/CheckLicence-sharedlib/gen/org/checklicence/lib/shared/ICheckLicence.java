/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/pprados/workspace.articles/CheckLicence/CheckLicence-sharedlib/src/org/checklicence/lib/shared/ICheckLicence.aidl
 */
package org.checklicence.lib.shared;
public interface ICheckLicence extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.checklicence.lib.shared.ICheckLicence
{
private static final java.lang.String DESCRIPTOR = "org.checklicence.lib.shared.ICheckLicence";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.checklicence.lib.shared.ICheckLicence interface,
 * generating a proxy if needed.
 */
public static org.checklicence.lib.shared.ICheckLicence asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.checklicence.lib.shared.ICheckLicence))) {
return ((org.checklicence.lib.shared.ICheckLicence)iin);
}
return new org.checklicence.lib.shared.ICheckLicence.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_checkLicence:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.checkLicence();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.checklicence.lib.shared.ICheckLicence
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public boolean checkLicence() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_checkLicence, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_checkLicence = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public boolean checkLicence() throws android.os.RemoteException;
}
