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
DECL|package|com.google.gerrit.sshd.commands
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
operator|.
name|commands
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|cache
operator|.
name|Cache
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
name|common
operator|.
name|data
operator|.
name|GlobalCapability
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
name|IdentifiedUser
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
name|sshd
operator|.
name|BaseCommand
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
name|sshd
operator|.
name|RequiresCapability
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
name|Provider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_comment
comment|/** Causes the caches to purge all entries and reload. */
end_comment

begin_class
annotation|@
name|RequiresCapability
argument_list|(
name|GlobalCapability
operator|.
name|FLUSH_CACHES
argument_list|)
DECL|class|FlushCaches
specifier|final
class|class
name|FlushCaches
extends|extends
name|CacheCommand
block|{
DECL|field|WEB_SESSIONS
specifier|private
specifier|static
specifier|final
name|String
name|WEB_SESSIONS
init|=
literal|"web_sessions"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--cache"
argument_list|,
name|usage
operator|=
literal|"flush named cache"
argument_list|,
name|metaVar
operator|=
literal|"NAME"
argument_list|)
DECL|field|caches
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|caches
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--all"
argument_list|,
name|usage
operator|=
literal|"flush all caches"
argument_list|)
DECL|field|all
specifier|private
name|boolean
name|all
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--list"
argument_list|,
name|usage
operator|=
literal|"list available caches"
argument_list|)
DECL|field|list
specifier|private
name|boolean
name|list
decl_stmt|;
annotation|@
name|Inject
DECL|field|currentUser
name|IdentifiedUser
name|currentUser
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|protected
name|void
name|run
parameter_list|()
throws|throws
name|Failure
block|{
if|if
condition|(
name|caches
operator|.
name|contains
argument_list|(
name|WEB_SESSIONS
argument_list|)
operator|&&
operator|!
name|currentUser
operator|.
name|getCapabilities
argument_list|()
operator|.
name|canAdministrateServer
argument_list|()
condition|)
block|{
name|String
name|msg
init|=
name|String
operator|.
name|format
argument_list|(
literal|"fatal: only site administrators can flush %s"
argument_list|,
name|WEB_SESSIONS
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|UnloggedFailure
argument_list|(
name|BaseCommand
operator|.
name|STATUS_NOT_ADMIN
argument_list|,
name|msg
argument_list|)
throw|;
block|}
if|if
condition|(
name|list
condition|)
block|{
if|if
condition|(
name|all
operator|||
name|caches
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
throw|throw
name|error
argument_list|(
literal|"error: cannot use --list with --all or --cache"
argument_list|)
throw|;
block|}
name|doList
argument_list|()
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|all
operator|&&
name|caches
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
throw|throw
name|error
argument_list|(
literal|"error: cannot combine --all and --cache"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|all
operator|&&
name|caches
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|caches
operator|.
name|contains
argument_list|(
literal|"all"
argument_list|)
condition|)
block|{
name|caches
operator|.
name|clear
argument_list|()
expr_stmt|;
name|all
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|all
operator|&&
name|caches
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|all
operator|=
literal|true
expr_stmt|;
block|}
specifier|final
name|SortedSet
argument_list|<
name|String
argument_list|>
name|names
init|=
name|cacheNames
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|String
name|n
range|:
name|caches
control|)
block|{
if|if
condition|(
operator|!
name|names
operator|.
name|contains
argument_list|(
name|n
argument_list|)
condition|)
block|{
throw|throw
name|error
argument_list|(
literal|"error: cache \""
operator|+
name|n
operator|+
literal|"\" not recognized"
argument_list|)
throw|;
block|}
block|}
name|doBulkFlush
argument_list|()
expr_stmt|;
block|}
DECL|method|error (final String msg)
specifier|private
specifier|static
name|UnloggedFailure
name|error
parameter_list|(
specifier|final
name|String
name|msg
parameter_list|)
block|{
return|return
operator|new
name|UnloggedFailure
argument_list|(
literal|1
argument_list|,
name|msg
argument_list|)
return|;
block|}
DECL|method|doList ()
specifier|private
name|void
name|doList
parameter_list|()
block|{
for|for
control|(
specifier|final
name|String
name|name
range|:
name|cacheNames
argument_list|()
control|)
block|{
name|stderr
operator|.
name|print
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|stderr
operator|.
name|print
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
name|stderr
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
DECL|method|doBulkFlush ()
specifier|private
name|void
name|doBulkFlush
parameter_list|()
block|{
try|try
block|{
for|for
control|(
name|String
name|plugin
range|:
name|cacheMap
operator|.
name|plugins
argument_list|()
control|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|Cache
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
argument_list|>
name|entry
range|:
name|cacheMap
operator|.
name|byPlugin
argument_list|(
name|plugin
argument_list|)
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|n
init|=
name|cacheNameOf
argument_list|(
name|plugin
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|flush
argument_list|(
name|n
argument_list|)
condition|)
block|{
try|try
block|{
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|invalidateAll
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|err
parameter_list|)
block|{
name|stderr
operator|.
name|println
argument_list|(
literal|"error: cannot flush cache \""
operator|+
name|n
operator|+
literal|"\": "
operator|+
name|err
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
finally|finally
block|{
name|stderr
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|flush (final String cacheName)
specifier|private
name|boolean
name|flush
parameter_list|(
specifier|final
name|String
name|cacheName
parameter_list|)
block|{
if|if
condition|(
name|caches
operator|.
name|contains
argument_list|(
name|cacheName
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|all
condition|)
block|{
if|if
condition|(
name|WEB_SESSIONS
operator|.
name|equals
argument_list|(
name|cacheName
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

