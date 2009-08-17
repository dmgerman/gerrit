begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|ProvisionException
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|CacheManager
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|config
operator|.
name|CacheConfiguration
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|config
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|config
operator|.
name|DiskStoreConfiguration
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|store
operator|.
name|MemoryStoreEvictionPolicy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_class
DECL|class|CacheManagerProvider
specifier|public
class|class
name|CacheManagerProvider
implements|implements
name|Provider
argument_list|<
name|CacheManager
argument_list|>
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CacheManagerProvider
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|config
specifier|private
specifier|final
name|Config
name|config
decl_stmt|;
DECL|field|sitePath
specifier|private
specifier|final
name|File
name|sitePath
decl_stmt|;
DECL|field|authConfig
specifier|private
specifier|final
name|AuthConfig
name|authConfig
decl_stmt|;
annotation|@
name|Inject
DECL|method|CacheManagerProvider (@erritServerConfig final Config cfg, @SitePath final File sitePath, final AuthConfig authConfig)
name|CacheManagerProvider
parameter_list|(
annotation|@
name|GerritServerConfig
specifier|final
name|Config
name|cfg
parameter_list|,
annotation|@
name|SitePath
specifier|final
name|File
name|sitePath
parameter_list|,
specifier|final
name|AuthConfig
name|authConfig
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|cfg
expr_stmt|;
name|this
operator|.
name|sitePath
operator|=
name|sitePath
expr_stmt|;
name|this
operator|.
name|authConfig
operator|=
name|authConfig
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|CacheManager
name|get
parameter_list|()
block|{
return|return
operator|new
name|CacheManager
argument_list|(
operator|new
name|Factory
argument_list|()
operator|.
name|toConfiguration
argument_list|()
argument_list|)
return|;
block|}
DECL|class|Factory
specifier|private
class|class
name|Factory
block|{
DECL|field|MB
specifier|private
specifier|static
specifier|final
name|int
name|MB
init|=
literal|1024
operator|*
literal|1024
decl_stmt|;
DECL|field|ONE_DAY
specifier|private
specifier|static
specifier|final
name|int
name|ONE_DAY
init|=
literal|24
operator|*
literal|60
decl_stmt|;
DECL|field|D_MAXAGE
specifier|private
specifier|static
specifier|final
name|int
name|D_MAXAGE
init|=
literal|3
operator|*
literal|30
operator|*
name|ONE_DAY
decl_stmt|;
DECL|field|D_SESSIONAGE
specifier|private
specifier|static
specifier|final
name|int
name|D_SESSIONAGE
init|=
name|ONE_DAY
operator|/
literal|2
decl_stmt|;
DECL|field|mgr
specifier|private
specifier|final
name|Configuration
name|mgr
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
DECL|method|toConfiguration ()
name|Configuration
name|toConfiguration
parameter_list|()
block|{
name|configureDiskStore
argument_list|()
expr_stmt|;
name|configureDefaultCache
argument_list|()
expr_stmt|;
switch|switch
condition|(
name|authConfig
operator|.
name|getLoginType
argument_list|()
condition|)
block|{
case|case
name|OPENID
case|:
name|mgr
operator|.
name|addCache
argument_list|(
name|ttl
argument_list|(
name|named
argument_list|(
literal|"openid"
argument_list|)
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
name|mgr
operator|.
name|addCache
argument_list|(
name|named
argument_list|(
literal|"accounts"
argument_list|)
argument_list|)
expr_stmt|;
name|mgr
operator|.
name|addCache
argument_list|(
name|named
argument_list|(
literal|"accounts_byemail"
argument_list|)
argument_list|)
expr_stmt|;
name|mgr
operator|.
name|addCache
argument_list|(
name|disk
argument_list|(
name|named
argument_list|(
literal|"diff"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|mgr
operator|.
name|addCache
argument_list|(
name|named
argument_list|(
literal|"groups"
argument_list|)
argument_list|)
expr_stmt|;
name|mgr
operator|.
name|addCache
argument_list|(
name|named
argument_list|(
literal|"projects"
argument_list|)
argument_list|)
expr_stmt|;
name|mgr
operator|.
name|addCache
argument_list|(
name|named
argument_list|(
literal|"sshkeys"
argument_list|)
argument_list|)
expr_stmt|;
name|mgr
operator|.
name|addCache
argument_list|(
name|disk
argument_list|(
name|tti
argument_list|(
name|named
argument_list|(
literal|"web_sessions"
argument_list|)
argument_list|,
name|D_SESSIONAGE
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|mgr
return|;
block|}
DECL|method|configureDiskStore ()
specifier|private
name|void
name|configureDiskStore
parameter_list|()
block|{
name|String
name|path
init|=
name|config
operator|.
name|getString
argument_list|(
literal|"cache"
argument_list|,
literal|null
argument_list|,
literal|"directory"
argument_list|)
decl_stmt|;
if|if
condition|(
name|path
operator|==
literal|null
operator|||
name|path
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|File
name|loc
init|=
operator|new
name|File
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|loc
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
name|loc
operator|=
operator|new
name|File
argument_list|(
name|sitePath
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|loc
operator|.
name|exists
argument_list|()
operator|||
name|loc
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
if|if
condition|(
name|loc
operator|.
name|canWrite
argument_list|()
condition|)
block|{
specifier|final
name|DiskStoreConfiguration
name|c
init|=
operator|new
name|DiskStoreConfiguration
argument_list|()
decl_stmt|;
name|c
operator|.
name|setPath
argument_list|(
name|loc
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|mgr
operator|.
name|addDiskStore
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Enabling disk cache "
operator|+
name|loc
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Can't write to disk cache: "
operator|+
name|loc
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Can't create disk cache: "
operator|+
name|loc
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|configureDefaultCache ()
specifier|private
name|void
name|configureDefaultCache
parameter_list|()
block|{
specifier|final
name|CacheConfiguration
name|c
init|=
operator|new
name|CacheConfiguration
argument_list|()
decl_stmt|;
name|c
operator|.
name|setMaxElementsInMemory
argument_list|(
name|config
operator|.
name|getInt
argument_list|(
literal|"cache"
argument_list|,
literal|"memorylimit"
argument_list|,
literal|1024
argument_list|)
argument_list|)
expr_stmt|;
name|c
operator|.
name|setMemoryStoreEvictionPolicyFromObject
argument_list|(
name|MemoryStoreEvictionPolicy
operator|.
name|LFU
argument_list|)
expr_stmt|;
name|c
operator|.
name|setTimeToIdleSeconds
argument_list|(
name|config
operator|.
name|getInt
argument_list|(
literal|"cache"
argument_list|,
literal|"maxage"
argument_list|,
name|D_MAXAGE
argument_list|)
operator|*
literal|60
argument_list|)
expr_stmt|;
name|c
operator|.
name|setTimeToLiveSeconds
argument_list|(
name|c
operator|.
name|getTimeToIdleSeconds
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|.
name|setEternal
argument_list|(
name|c
operator|.
name|getTimeToIdleSeconds
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|mgr
operator|.
name|getDiskStoreConfiguration
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|setMaxElementsOnDisk
argument_list|(
name|config
operator|.
name|getInt
argument_list|(
literal|"cache"
argument_list|,
literal|"disklimit"
argument_list|,
literal|16384
argument_list|)
argument_list|)
expr_stmt|;
name|c
operator|.
name|setOverflowToDisk
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|c
operator|.
name|setDiskPersistent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
specifier|final
name|int
name|diskbuffer
init|=
name|config
operator|.
name|getInt
argument_list|(
literal|"cache"
argument_list|,
literal|"diskbuffer"
argument_list|,
literal|5
operator|*
name|MB
argument_list|)
decl_stmt|;
name|c
operator|.
name|setDiskSpoolBufferSizeMB
argument_list|(
name|Math
operator|.
name|max
argument_list|(
literal|1
argument_list|,
name|diskbuffer
operator|/
name|MB
argument_list|)
argument_list|)
expr_stmt|;
name|c
operator|.
name|setDiskExpiryThreadIntervalSeconds
argument_list|(
literal|60
operator|*
literal|60
argument_list|)
expr_stmt|;
block|}
name|mgr
operator|.
name|setDefaultCacheConfiguration
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
DECL|method|named (final String name)
specifier|private
name|CacheConfiguration
name|named
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
specifier|final
name|CacheConfiguration
name|c
init|=
name|newCache
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|int
name|e
init|=
name|c
operator|.
name|getMaxElementsInMemory
argument_list|()
decl_stmt|;
name|c
operator|.
name|setMaxElementsInMemory
argument_list|(
name|config
operator|.
name|getInt
argument_list|(
literal|"cache"
argument_list|,
name|name
argument_list|,
literal|"memorylimit"
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|ttl
argument_list|(
name|c
argument_list|,
operator|(
name|int
operator|)
name|c
operator|.
name|getTimeToIdleSeconds
argument_list|()
operator|/
literal|60
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
DECL|method|newCache (final String name)
specifier|private
name|CacheConfiguration
name|newCache
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
try|try
block|{
specifier|final
name|CacheConfiguration
name|c
decl_stmt|;
name|c
operator|=
name|mgr
operator|.
name|getDefaultCacheConfiguration
argument_list|()
operator|.
name|clone
argument_list|()
expr_stmt|;
name|c
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Cannot configure cache "
operator|+
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|ttl (final CacheConfiguration c, final int age)
specifier|private
name|CacheConfiguration
name|ttl
parameter_list|(
specifier|final
name|CacheConfiguration
name|c
parameter_list|,
specifier|final
name|int
name|age
parameter_list|)
block|{
specifier|final
name|String
name|name
init|=
name|c
operator|.
name|getName
argument_list|()
decl_stmt|;
name|c
operator|.
name|setTimeToIdleSeconds
argument_list|(
name|config
operator|.
name|getInt
argument_list|(
literal|"cache"
argument_list|,
name|name
argument_list|,
literal|"maxage"
argument_list|,
name|age
argument_list|)
operator|*
literal|60
argument_list|)
expr_stmt|;
name|c
operator|.
name|setTimeToLiveSeconds
argument_list|(
name|c
operator|.
name|getTimeToIdleSeconds
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|.
name|setEternal
argument_list|(
name|c
operator|.
name|getTimeToIdleSeconds
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
DECL|method|tti (final CacheConfiguration c, final int age)
specifier|private
name|CacheConfiguration
name|tti
parameter_list|(
specifier|final
name|CacheConfiguration
name|c
parameter_list|,
specifier|final
name|int
name|age
parameter_list|)
block|{
specifier|final
name|String
name|name
init|=
name|c
operator|.
name|getName
argument_list|()
decl_stmt|;
name|c
operator|.
name|setTimeToIdleSeconds
argument_list|(
name|config
operator|.
name|getInt
argument_list|(
literal|"cache"
argument_list|,
name|name
argument_list|,
literal|"maxage"
argument_list|,
name|age
argument_list|)
operator|*
literal|60
argument_list|)
expr_stmt|;
name|c
operator|.
name|setTimeToLiveSeconds
argument_list|(
literal|0
comment|/* until idle out, or removed */
argument_list|)
expr_stmt|;
name|c
operator|.
name|setEternal
argument_list|(
name|c
operator|.
name|getTimeToIdleSeconds
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
DECL|method|disk (final CacheConfiguration c)
specifier|private
name|CacheConfiguration
name|disk
parameter_list|(
specifier|final
name|CacheConfiguration
name|c
parameter_list|)
block|{
specifier|final
name|String
name|name
init|=
name|c
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|mgr
operator|.
name|getDiskStoreConfiguration
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|int
name|e
init|=
name|c
operator|.
name|getMaxElementsOnDisk
argument_list|()
decl_stmt|;
name|c
operator|.
name|setMaxElementsOnDisk
argument_list|(
name|config
operator|.
name|getInt
argument_list|(
literal|"cache"
argument_list|,
name|name
argument_list|,
literal|"disklimit"
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|buffer
init|=
name|c
operator|.
name|getDiskSpoolBufferSizeMB
argument_list|()
operator|*
name|MB
decl_stmt|;
name|buffer
operator|=
name|config
operator|.
name|getInt
argument_list|(
literal|"cache"
argument_list|,
name|name
argument_list|,
literal|"diskbuffer"
argument_list|,
name|buffer
argument_list|)
operator|/
name|MB
expr_stmt|;
name|c
operator|.
name|setDiskSpoolBufferSizeMB
argument_list|(
name|Math
operator|.
name|max
argument_list|(
literal|1
argument_list|,
name|buffer
argument_list|)
argument_list|)
expr_stmt|;
name|c
operator|.
name|setOverflowToDisk
argument_list|(
name|c
operator|.
name|getMaxElementsOnDisk
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|c
operator|.
name|setDiskPersistent
argument_list|(
name|c
operator|.
name|getMaxElementsOnDisk
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
block|}
block|}
end_class

end_unit

