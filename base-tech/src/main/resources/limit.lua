local key = KEYS[1] --限流key (一秒一个)
local limit = tonumber(ARGV[1]) --限流大小
local current = tonumber(redis.call('get', key) or '0')
if current + 1 > limit then --如果超过限流大小
  return 0
else --请求数+1，并设置过期时间2秒
  redis.call('INCRBY', key, "1")
  redis.call('expire', key, "2")
  return 1
end
