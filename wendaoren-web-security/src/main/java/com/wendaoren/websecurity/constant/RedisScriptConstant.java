package com.wendaoren.websecurity.constant;

import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

public interface RedisScriptConstant {

    /**
     * 删除会话时同步删除其互斥数据Lua
     */
    String SESSION_DEL_MUTEX_DATA_BY_SESSIONID_LUA = "local values = redis.call('MGET', unpack(KEYS)) "
            + "for i, value in ipairs(values) do "
            + "  if value == ARGV[1] then "
            + "    redis.call('DEL', KEYS[i]) "
            + "  end "
            + "end";
    RedisScript<Void> SESSION_DEL_MUTEX_DATA_BY_SESSIONID = new DefaultRedisScript<>(SESSION_DEL_MUTEX_DATA_BY_SESSIONID_LUA, Void.class);
}
