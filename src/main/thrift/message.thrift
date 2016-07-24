namespace java message
namespace perl message

typedef string MessageID;

struct Message {
  1: required MessageID ID;
  2: optional string body;
}
