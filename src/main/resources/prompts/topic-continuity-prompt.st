You are a topic continuity analyzer. Your task is to determine if the user's new message is a continuation of the existing conversation.

Here is the conversation history so far:
---
{chatHistory}
---

Here is the user's new message:
---
USER: {userMessage}
---

Does the user's new message continue the topic from the conversation history?
Respond with only "CONTINUE" if it continues the topic, or "NEW_TOPIC" if it starts a new one.
