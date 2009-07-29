begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|data
operator|.
name|AccountCache
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|data
operator|.
name|GroupCache
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|data
operator|.
name|ProjectCache
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|reviewdb
operator|.
name|ReviewDb
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|reviewdb
operator|.
name|SystemConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|reviewdb
operator|.
name|SystemConfig
operator|.
name|LoginType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|rpc
operator|.
name|Common
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|GerritServerConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|SitePath
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|patch
operator|.
name|DiffCacheEntryFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ssh
operator|.
name|SshKeyCacheEntryFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|server
operator|.
name|SignedToken
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|server
operator|.
name|XsrfException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|jdbc
operator|.
name|Database
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
name|Singleton
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
name|Cache
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
name|Ehcache
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
name|constructs
operator|.
name|blocking
operator|.
name|SelfPopulatingCache
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
name|errors
operator|.
name|RepositoryNotFoundException
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
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|PersonIdent
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
name|Repository
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
name|RepositoryCache
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
name|UserConfig
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
name|RepositoryCache
operator|.
name|FileKey
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_comment
comment|/** Global server-side state for Gerrit. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|GerritServer
specifier|public
class|class
name|GerritServer
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
name|GerritServer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|cacheMgr
specifier|private
specifier|static
name|CacheManager
name|cacheMgr
decl_stmt|;
DECL|method|closeCacheManager ()
specifier|static
name|void
name|closeCacheManager
parameter_list|()
block|{
if|if
condition|(
name|cacheMgr
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|cacheMgr
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|bad
parameter_list|)
block|{       }
finally|finally
block|{
name|cacheMgr
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
DECL|method|serverUrl (final HttpServletRequest req)
specifier|public
specifier|static
name|String
name|serverUrl
parameter_list|(
specifier|final
name|HttpServletRequest
name|req
parameter_list|)
block|{
comment|// Assume this servlet is in the context with a simple name like "login"
comment|// and we were accessed without any path info. Clipping the last part of
comment|// the name from the URL should generate the web application's root path.
comment|//
name|String
name|uri
init|=
name|req
operator|.
name|getRequestURL
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
specifier|final
name|int
name|s
init|=
name|uri
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|>=
literal|0
condition|)
block|{
name|uri
operator|=
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|s
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|final
name|String
name|sfx
init|=
literal|"/gerrit/rpc/"
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|endsWith
argument_list|(
name|sfx
argument_list|)
condition|)
block|{
comment|// Nope, it was one of our RPC servlets. Drop the rpc too.
comment|//
name|uri
operator|=
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|uri
operator|.
name|length
argument_list|()
operator|-
operator|(
name|sfx
operator|.
name|length
argument_list|()
operator|-
literal|1
operator|)
argument_list|)
expr_stmt|;
block|}
return|return
name|uri
return|;
block|}
DECL|field|db
specifier|private
specifier|final
name|Database
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|sitePath
specifier|private
specifier|final
name|File
name|sitePath
decl_stmt|;
DECL|field|gerritConfigFile
specifier|private
specifier|final
name|Config
name|gerritConfigFile
decl_stmt|;
DECL|field|sessionAge
specifier|private
specifier|final
name|int
name|sessionAge
decl_stmt|;
DECL|field|xsrf
specifier|private
specifier|final
name|SignedToken
name|xsrf
decl_stmt|;
DECL|field|account
specifier|private
specifier|final
name|SignedToken
name|account
decl_stmt|;
DECL|field|emailReg
specifier|private
specifier|final
name|SignedToken
name|emailReg
decl_stmt|;
DECL|field|basepath
specifier|private
specifier|final
name|File
name|basepath
decl_stmt|;
DECL|field|diffCache
specifier|private
specifier|final
name|SelfPopulatingCache
name|diffCache
decl_stmt|;
DECL|field|sshKeysCache
specifier|private
specifier|final
name|SelfPopulatingCache
name|sshKeysCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|GerritServer (final Database<ReviewDb> database, final SystemConfig sConfig, @SitePath final File path, @GerritServerConfig final Config cfg)
name|GerritServer
parameter_list|(
specifier|final
name|Database
argument_list|<
name|ReviewDb
argument_list|>
name|database
parameter_list|,
specifier|final
name|SystemConfig
name|sConfig
parameter_list|,
annotation|@
name|SitePath
specifier|final
name|File
name|path
parameter_list|,
annotation|@
name|GerritServerConfig
specifier|final
name|Config
name|cfg
parameter_list|)
throws|throws
name|XsrfException
block|{
name|db
operator|=
name|database
expr_stmt|;
name|sitePath
operator|=
name|path
expr_stmt|;
name|gerritConfigFile
operator|=
name|cfg
expr_stmt|;
name|sessionAge
operator|=
name|cfg
operator|.
name|getInt
argument_list|(
literal|"auth"
argument_list|,
literal|"maxsessionage"
argument_list|,
literal|12
operator|*
literal|60
argument_list|)
operator|*
literal|60
expr_stmt|;
name|xsrf
operator|=
operator|new
name|SignedToken
argument_list|(
name|getSessionAge
argument_list|()
argument_list|,
name|sConfig
operator|.
name|xsrfPrivateKey
argument_list|)
expr_stmt|;
specifier|final
name|int
name|accountCookieAge
decl_stmt|;
switch|switch
condition|(
name|getLoginType
argument_list|()
condition|)
block|{
case|case
name|HTTP
case|:
name|accountCookieAge
operator|=
operator|-
literal|1
expr_stmt|;
comment|// expire when the browser closes
break|break;
case|case
name|OPENID
case|:
default|default:
name|accountCookieAge
operator|=
name|getSessionAge
argument_list|()
expr_stmt|;
break|break;
block|}
name|account
operator|=
operator|new
name|SignedToken
argument_list|(
name|accountCookieAge
argument_list|,
name|sConfig
operator|.
name|accountPrivateKey
argument_list|)
expr_stmt|;
name|emailReg
operator|=
operator|new
name|SignedToken
argument_list|(
literal|5
operator|*
literal|24
operator|*
literal|60
operator|*
literal|60
argument_list|,
name|sConfig
operator|.
name|accountPrivateKey
argument_list|)
expr_stmt|;
specifier|final
name|String
name|basePath
init|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"basepath"
argument_list|)
decl_stmt|;
if|if
condition|(
name|basePath
operator|!=
literal|null
condition|)
block|{
name|File
name|root
init|=
operator|new
name|File
argument_list|(
name|basePath
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|root
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
name|root
operator|=
operator|new
name|File
argument_list|(
name|sitePath
argument_list|,
name|basePath
argument_list|)
expr_stmt|;
block|}
name|basepath
operator|=
name|root
expr_stmt|;
block|}
else|else
block|{
name|basepath
operator|=
literal|null
expr_stmt|;
block|}
name|Common
operator|.
name|setSchemaFactory
argument_list|(
name|db
argument_list|)
expr_stmt|;
name|Common
operator|.
name|setProjectCache
argument_list|(
operator|new
name|ProjectCache
argument_list|()
argument_list|)
expr_stmt|;
name|Common
operator|.
name|setAccountCache
argument_list|(
operator|new
name|AccountCache
argument_list|()
argument_list|)
expr_stmt|;
name|Common
operator|.
name|setGroupCache
argument_list|(
operator|new
name|GroupCache
argument_list|(
name|sConfig
argument_list|)
argument_list|)
expr_stmt|;
name|cacheMgr
operator|=
operator|new
name|CacheManager
argument_list|(
name|createCacheConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|diffCache
operator|=
name|startCacheDiff
argument_list|()
expr_stmt|;
name|sshKeysCache
operator|=
name|startCacheSshKeys
argument_list|()
expr_stmt|;
block|}
DECL|method|createCacheConfiguration ()
specifier|private
name|Configuration
name|createCacheConfiguration
parameter_list|()
block|{
specifier|final
name|Configuration
name|mgrCfg
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|configureDiskStore
argument_list|(
name|mgrCfg
argument_list|)
expr_stmt|;
name|configureDefaultCache
argument_list|(
name|mgrCfg
argument_list|)
expr_stmt|;
if|if
condition|(
name|getLoginType
argument_list|()
operator|==
name|LoginType
operator|.
name|OPENID
condition|)
block|{
specifier|final
name|CacheConfiguration
name|c
decl_stmt|;
name|c
operator|=
name|configureNamedCache
argument_list|(
name|mgrCfg
argument_list|,
literal|"openid"
argument_list|,
literal|false
argument_list|,
literal|5
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
name|mgrCfg
operator|.
name|addCache
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
name|mgrCfg
operator|.
name|addCache
argument_list|(
name|configureNamedCache
argument_list|(
name|mgrCfg
argument_list|,
literal|"diff"
argument_list|,
literal|true
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|mgrCfg
operator|.
name|addCache
argument_list|(
name|configureNamedCache
argument_list|(
name|mgrCfg
argument_list|,
literal|"sshkeys"
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|mgrCfg
return|;
block|}
DECL|method|configureDiskStore (final Configuration mgrCfg)
specifier|private
name|void
name|configureDiskStore
parameter_list|(
specifier|final
name|Configuration
name|mgrCfg
parameter_list|)
block|{
name|String
name|path
init|=
name|gerritConfigFile
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
name|path
operator|=
literal|"disk_cache"
expr_stmt|;
block|}
specifier|final
name|File
name|loc
init|=
operator|new
name|File
argument_list|(
name|sitePath
argument_list|,
name|path
argument_list|)
decl_stmt|;
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
name|mgrCfg
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
DECL|method|configureDefaultCache (final Configuration mgrCfg)
specifier|private
name|void
name|configureDefaultCache
parameter_list|(
specifier|final
name|Configuration
name|mgrCfg
parameter_list|)
block|{
specifier|final
name|Config
name|i
init|=
name|gerritConfigFile
decl_stmt|;
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
name|i
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
name|setTimeToLiveSeconds
argument_list|(
literal|0
argument_list|)
expr_stmt|;
specifier|final
name|int
name|oneday
init|=
literal|24
operator|*
literal|60
decl_stmt|;
name|c
operator|.
name|setTimeToIdleSeconds
argument_list|(
name|i
operator|.
name|getInt
argument_list|(
literal|"cache"
argument_list|,
literal|"maxage"
argument_list|,
literal|3
operator|*
literal|30
operator|*
name|oneday
argument_list|)
operator|*
literal|60
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
name|mgrCfg
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
name|i
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
name|int
name|diskbuffer
init|=
name|i
operator|.
name|getInt
argument_list|(
literal|"cache"
argument_list|,
literal|"diskbuffer"
argument_list|,
literal|5
operator|*
literal|1024
operator|*
literal|1024
argument_list|)
decl_stmt|;
name|diskbuffer
operator|/=
literal|1024
operator|*
literal|1024
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
name|diskbuffer
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
name|mgrCfg
operator|.
name|setDefaultCacheConfiguration
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
DECL|method|configureNamedCache (final Configuration mgrCfg, final String name, final boolean disk, final int defaultAge)
specifier|private
name|CacheConfiguration
name|configureNamedCache
parameter_list|(
specifier|final
name|Configuration
name|mgrCfg
parameter_list|,
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|boolean
name|disk
parameter_list|,
specifier|final
name|int
name|defaultAge
parameter_list|)
block|{
specifier|final
name|Config
name|i
init|=
name|gerritConfigFile
decl_stmt|;
specifier|final
name|CacheConfiguration
name|def
init|=
name|mgrCfg
operator|.
name|getDefaultCacheConfiguration
argument_list|()
decl_stmt|;
specifier|final
name|CacheConfiguration
name|cfg
decl_stmt|;
try|try
block|{
name|cfg
operator|=
name|def
operator|.
name|clone
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Cannot configure cache "
operator|+
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|cfg
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setMaxElementsInMemory
argument_list|(
name|i
operator|.
name|getInt
argument_list|(
literal|"cache"
argument_list|,
name|name
argument_list|,
literal|"memorylimit"
argument_list|,
name|def
operator|.
name|getMaxElementsInMemory
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setTimeToIdleSeconds
argument_list|(
name|i
operator|.
name|getInt
argument_list|(
literal|"cache"
argument_list|,
name|name
argument_list|,
literal|"maxage"
argument_list|,
name|defaultAge
operator|>
literal|0
condition|?
name|defaultAge
else|:
call|(
name|int
call|)
argument_list|(
name|def
operator|.
name|getTimeToIdleSeconds
argument_list|()
operator|/
literal|60
argument_list|)
argument_list|)
operator|*
literal|60
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setEternal
argument_list|(
name|cfg
operator|.
name|getTimeToIdleSeconds
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|disk
operator|&&
name|mgrCfg
operator|.
name|getDiskStoreConfiguration
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cfg
operator|.
name|setMaxElementsOnDisk
argument_list|(
name|i
operator|.
name|getInt
argument_list|(
literal|"cache"
argument_list|,
name|name
argument_list|,
literal|"disklimit"
argument_list|,
name|def
operator|.
name|getMaxElementsOnDisk
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|int
name|m
init|=
literal|1024
operator|*
literal|1024
decl_stmt|;
specifier|final
name|int
name|diskbuffer
init|=
name|i
operator|.
name|getInt
argument_list|(
literal|"cache"
argument_list|,
name|name
argument_list|,
literal|"diskbuffer"
argument_list|,
name|def
operator|.
name|getDiskSpoolBufferSizeMB
argument_list|()
operator|*
name|m
argument_list|)
operator|/
name|m
decl_stmt|;
name|cfg
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
argument_list|)
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setOverflowToDisk
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setDiskPersistent
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|cfg
return|;
block|}
DECL|method|startCacheDiff ()
specifier|private
name|SelfPopulatingCache
name|startCacheDiff
parameter_list|()
block|{
specifier|final
name|Cache
name|dc
init|=
name|cacheMgr
operator|.
name|getCache
argument_list|(
literal|"diff"
argument_list|)
decl_stmt|;
specifier|final
name|SelfPopulatingCache
name|r
decl_stmt|;
name|r
operator|=
operator|new
name|SelfPopulatingCache
argument_list|(
name|dc
argument_list|,
operator|new
name|DiffCacheEntryFactory
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|cacheMgr
operator|.
name|replaceCacheWithDecoratedCache
argument_list|(
name|dc
argument_list|,
name|r
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
DECL|method|startCacheSshKeys ()
specifier|private
name|SelfPopulatingCache
name|startCacheSshKeys
parameter_list|()
block|{
specifier|final
name|Cache
name|dc
init|=
name|cacheMgr
operator|.
name|getCache
argument_list|(
literal|"sshkeys"
argument_list|)
decl_stmt|;
specifier|final
name|SelfPopulatingCache
name|r
decl_stmt|;
name|r
operator|=
operator|new
name|SelfPopulatingCache
argument_list|(
name|dc
argument_list|,
operator|new
name|SshKeyCacheEntryFactory
argument_list|(
name|db
argument_list|)
argument_list|)
expr_stmt|;
name|cacheMgr
operator|.
name|replaceCacheWithDecoratedCache
argument_list|(
name|dc
argument_list|,
name|r
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
comment|/** Time (in seconds) that user sessions stay "signed in". */
DECL|method|getSessionAge ()
specifier|public
name|int
name|getSessionAge
parameter_list|()
block|{
return|return
name|sessionAge
return|;
block|}
comment|/** Get the signature support used to protect against XSRF attacks. */
DECL|method|getXsrfToken ()
specifier|public
name|SignedToken
name|getXsrfToken
parameter_list|()
block|{
return|return
name|xsrf
return|;
block|}
comment|/** Get the signature support used to protect user identity cookies. */
DECL|method|getAccountToken ()
specifier|public
name|SignedToken
name|getAccountToken
parameter_list|()
block|{
return|return
name|account
return|;
block|}
comment|/** Get the signature used for email registration/validation links. */
DECL|method|getEmailRegistrationToken ()
specifier|public
name|SignedToken
name|getEmailRegistrationToken
parameter_list|()
block|{
return|return
name|emailReg
return|;
block|}
DECL|method|getLoginType ()
specifier|public
name|SystemConfig
operator|.
name|LoginType
name|getLoginType
parameter_list|()
block|{
name|String
name|type
init|=
name|getGerritConfig
argument_list|()
operator|.
name|getString
argument_list|(
literal|"auth"
argument_list|,
literal|null
argument_list|,
literal|"type"
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
return|return
name|SystemConfig
operator|.
name|LoginType
operator|.
name|OPENID
return|;
block|}
for|for
control|(
name|SystemConfig
operator|.
name|LoginType
name|t
range|:
name|SystemConfig
operator|.
name|LoginType
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|type
operator|.
name|equalsIgnoreCase
argument_list|(
name|t
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|t
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unsupported auth.type: "
operator|+
name|type
argument_list|)
throw|;
block|}
DECL|method|getLoginHttpHeader ()
specifier|public
name|String
name|getLoginHttpHeader
parameter_list|()
block|{
return|return
name|getGerritConfig
argument_list|()
operator|.
name|getString
argument_list|(
literal|"auth"
argument_list|,
literal|null
argument_list|,
literal|"httpheader"
argument_list|)
return|;
block|}
DECL|method|getEmailFormat ()
specifier|public
name|String
name|getEmailFormat
parameter_list|()
block|{
return|return
name|getGerritConfig
argument_list|()
operator|.
name|getString
argument_list|(
literal|"auth"
argument_list|,
literal|null
argument_list|,
literal|"emailformat"
argument_list|)
return|;
block|}
comment|/** Optional canonical URL for this application. */
DECL|method|getCanonicalURL ()
specifier|public
name|String
name|getCanonicalURL
parameter_list|()
block|{
name|String
name|u
init|=
name|getGerritConfig
argument_list|()
operator|.
name|getString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"canonicalweburl"
argument_list|)
decl_stmt|;
if|if
condition|(
name|u
operator|!=
literal|null
operator|&&
operator|!
name|u
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|u
operator|+=
literal|"/"
expr_stmt|;
block|}
return|return
name|u
return|;
block|}
DECL|method|getGerritConfig ()
specifier|private
name|Config
name|getGerritConfig
parameter_list|()
block|{
return|return
name|gerritConfigFile
return|;
block|}
comment|/**    * Get (or open) a repository by name.    *    * @param name the repository name, relative to the base directory.    * @return the cached Repository instance. Caller must call {@code close()}    *         when done to decrement the resource handle.    * @throws RepositoryNotFoundException the name does not denote an existing    *         repository, or the name cannot be read as a repository.    */
DECL|method|openRepository (String name)
specifier|public
name|Repository
name|openRepository
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|RepositoryNotFoundException
block|{
if|if
condition|(
name|basepath
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RepositoryNotFoundException
argument_list|(
literal|"No gerrit.basepath configured"
argument_list|)
throw|;
block|}
if|if
condition|(
name|isUnreasonableName
argument_list|(
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryNotFoundException
argument_list|(
literal|"Invalid name: "
operator|+
name|name
argument_list|)
throw|;
block|}
try|try
block|{
specifier|final
name|FileKey
name|loc
init|=
name|FileKey
operator|.
name|lenient
argument_list|(
operator|new
name|File
argument_list|(
name|basepath
argument_list|,
name|name
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|RepositoryCache
operator|.
name|open
argument_list|(
name|loc
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e1
parameter_list|)
block|{
specifier|final
name|RepositoryNotFoundException
name|e2
decl_stmt|;
name|e2
operator|=
operator|new
name|RepositoryNotFoundException
argument_list|(
literal|"Cannot open repository "
operator|+
name|name
argument_list|)
expr_stmt|;
name|e2
operator|.
name|initCause
argument_list|(
name|e1
argument_list|)
expr_stmt|;
throw|throw
name|e2
throw|;
block|}
block|}
DECL|method|isUnreasonableName (final String name)
specifier|private
name|boolean
name|isUnreasonableName
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
return|return
literal|true
return|;
comment|// no empty paths
if|if
condition|(
name|name
operator|.
name|indexOf
argument_list|(
literal|'\\'
argument_list|)
operator|>=
literal|0
condition|)
return|return
literal|true
return|;
comment|// no windows/dos stlye paths
if|if
condition|(
name|name
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|'/'
condition|)
return|return
literal|true
return|;
comment|// no absolute paths
if|if
condition|(
operator|new
name|File
argument_list|(
name|name
argument_list|)
operator|.
name|isAbsolute
argument_list|()
condition|)
return|return
literal|true
return|;
comment|// no absolute paths
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"../"
argument_list|)
condition|)
return|return
literal|true
return|;
comment|// no "l../etc/passwd"
if|if
condition|(
name|name
operator|.
name|contains
argument_list|(
literal|"/../"
argument_list|)
condition|)
return|return
literal|true
return|;
comment|// no "foo/../etc/passwd"
if|if
condition|(
name|name
operator|.
name|contains
argument_list|(
literal|"/./"
argument_list|)
condition|)
return|return
literal|true
return|;
comment|// "foo/./foo" is insane to ask
if|if
condition|(
name|name
operator|.
name|contains
argument_list|(
literal|"//"
argument_list|)
condition|)
return|return
literal|true
return|;
comment|// windows UNC path can be "//..."
return|return
literal|false
return|;
comment|// is a reasonable name
block|}
comment|/** Get all registered caches. */
DECL|method|getAllCaches ()
specifier|public
name|Ehcache
index|[]
name|getAllCaches
parameter_list|()
block|{
specifier|final
name|String
index|[]
name|cacheNames
init|=
name|cacheMgr
operator|.
name|getCacheNames
argument_list|()
decl_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|cacheNames
argument_list|)
expr_stmt|;
specifier|final
name|Ehcache
index|[]
name|r
init|=
operator|new
name|Ehcache
index|[
name|cacheNames
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|cacheNames
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|r
index|[
name|i
index|]
operator|=
name|cacheMgr
operator|.
name|getEhcache
argument_list|(
name|cacheNames
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
comment|/** Get any existing cache by name. */
DECL|method|getCache (final String name)
specifier|public
name|Cache
name|getCache
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
name|cacheMgr
operator|.
name|getCache
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/** Get the self-populating cache of DiffCacheContent entities. */
DECL|method|getDiffCache ()
specifier|public
name|SelfPopulatingCache
name|getDiffCache
parameter_list|()
block|{
return|return
name|diffCache
return|;
block|}
comment|/** Get the self-populating cache of user SSH keys. */
DECL|method|getSshKeysCache ()
specifier|public
name|SelfPopulatingCache
name|getSshKeysCache
parameter_list|()
block|{
return|return
name|sshKeysCache
return|;
block|}
comment|/** Get a new identity representing this Gerrit server in Git. */
DECL|method|newGerritPersonIdent ()
specifier|public
name|PersonIdent
name|newGerritPersonIdent
parameter_list|()
block|{
name|String
name|name
init|=
name|getGerritConfig
argument_list|()
operator|.
name|getString
argument_list|(
literal|"user"
argument_list|,
literal|null
argument_list|,
literal|"name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
literal|"Gerrit Code Review"
expr_stmt|;
block|}
name|String
name|email
init|=
name|getGerritConfig
argument_list|()
operator|.
name|get
argument_list|(
name|UserConfig
operator|.
name|KEY
argument_list|)
operator|.
name|getCommitterEmail
argument_list|()
decl_stmt|;
if|if
condition|(
name|email
operator|==
literal|null
operator|||
name|email
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|email
operator|=
literal|"gerrit@localhost"
expr_stmt|;
block|}
return|return
operator|new
name|PersonIdent
argument_list|(
name|name
argument_list|,
name|email
argument_list|)
return|;
block|}
DECL|method|isAllowGoogleAccountUpgrade ()
specifier|public
name|boolean
name|isAllowGoogleAccountUpgrade
parameter_list|()
block|{
return|return
name|getGerritConfig
argument_list|()
operator|.
name|getBoolean
argument_list|(
literal|"auth"
argument_list|,
literal|"allowgoogleaccountupgrade"
argument_list|,
literal|false
argument_list|)
return|;
block|}
block|}
end_class

end_unit

