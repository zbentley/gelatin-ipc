include "message.thrift"
include "rpc.thrift"

namespace cpp gelatin
namespace java gelatin
namespace perl gelatin

typedef map<string,string> ConfigurationInfo

struct PersistenceResult {
  1: optional message.MessageID ID;
}

enum RecoveryActionCode {
  RETRY_IMMEDIATE = 1;
  RETRY_WAIT = 2;
  RESTART = 3;
  RESET = 4;
  MANUAL_INTERVENTION = 5;
}

enum PersistenceErrorCode {
  BUSY = 1;
  UNKNOWN = 2;
}

enum ServiceErrorCode {
  STARTING = 1;
  STOPPING = 2;
  UNKNOWN = 3;
  EMPTY = 4; // Thrown when flushPendingRPC is done on an empty queue.
}

exception ServiceException {
  1: string duringOperation;
  2: ServiceErrorCode errno;
  3: RecoveryActionCode advise;
  4: optional string why;
  5: optional i32 waitMilliseconds;
  6: optional bool dataLoss;
}

exception PersistenceException {
  1: PersistenceErrorCode errno;
  2: RecoveryActionCode advise;
  3: optional string why;
  4: optional i32 waitMs;
}

exception ServiceRunningException {}

service Persistence {
    ConfigurationInfo getConfig();
    bool start() throws(1: ServiceException e);
    bool stop() throws(1: ServiceException e);

    PersistenceResult persist(1: message.Message message) throws (1: PersistenceException p, 2: ServiceException s);
    rpc.RPCResult persistRPC(1: message.Message message, 2: bool durableReply) throws (1: PersistenceException p, 2: ServiceException s, 3: rpc.RPCException r);

    rpc.RPCResult flushPendingRPC(1: bool continueOnError) throws(1: ServiceException s, 2: ServiceRunningException r, 3: rpc.RPCException e);
}

service RPC extends Persistence {
    PersistenceResult enqeueRPC(1: message.Message message) throws (1: PersistenceException p)
}