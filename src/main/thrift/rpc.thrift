include "message.thrift"

namespace java rpc
namespace perl rpc

enum ResultCode {
  SUCCESS = 1;
  FAILURE = 0;
  UNKNOWN = 3;
}

exception RPCException {
  1: i32 errno;
  3: optional string why;
}


struct RPCResult {
  1: required string ID;
  2: required message.MessageID messageID;
  3: optional string result;
  4: optional ResultCode code;
}
