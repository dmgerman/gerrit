begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
name|common
operator|.
name|collect
operator|.
name|ImmutableList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_comment
comment|/**  * This event is produced by {@link GerritServerConfigReloader} and forwarded to callers  * implementing {@link GerritConfigListener}.  *  *<p>The event intends to:  *  *<p>1. Help the callers figure out if any action should be taken, depending on which entries are  * updated in gerrit.config.  *  *<p>2. Provide the callers with a mechanism to accept/reject the entries of interest: @see  * accept(Set<ConfigKey> entries), @see accept(String section), @see reject(Set<ConfigKey> entries)  * (+ various overloaded versions of these)  */
end_comment

begin_class
DECL|class|ConfigUpdatedEvent
specifier|public
class|class
name|ConfigUpdatedEvent
block|{
DECL|field|oldConfig
specifier|private
specifier|final
name|Config
name|oldConfig
decl_stmt|;
DECL|field|newConfig
specifier|private
specifier|final
name|Config
name|newConfig
decl_stmt|;
DECL|method|ConfigUpdatedEvent (Config oldConfig, Config newConfig)
specifier|public
name|ConfigUpdatedEvent
parameter_list|(
name|Config
name|oldConfig
parameter_list|,
name|Config
name|newConfig
parameter_list|)
block|{
name|this
operator|.
name|oldConfig
operator|=
name|oldConfig
expr_stmt|;
name|this
operator|.
name|newConfig
operator|=
name|newConfig
expr_stmt|;
block|}
DECL|method|getOldConfig ()
specifier|public
name|Config
name|getOldConfig
parameter_list|()
block|{
return|return
name|this
operator|.
name|oldConfig
return|;
block|}
DECL|method|getNewConfig ()
specifier|public
name|Config
name|getNewConfig
parameter_list|()
block|{
return|return
name|this
operator|.
name|newConfig
return|;
block|}
DECL|method|accept (ConfigKey entry)
specifier|public
name|Update
name|accept
parameter_list|(
name|ConfigKey
name|entry
parameter_list|)
block|{
return|return
name|accept
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|entry
argument_list|)
argument_list|)
return|;
block|}
DECL|method|accept (Set<ConfigKey> entries)
specifier|public
name|Update
name|accept
parameter_list|(
name|Set
argument_list|<
name|ConfigKey
argument_list|>
name|entries
parameter_list|)
block|{
return|return
name|createUpdate
argument_list|(
name|entries
argument_list|,
name|UpdateResult
operator|.
name|APPLIED
argument_list|)
return|;
block|}
DECL|method|accept (String section)
specifier|public
name|Update
name|accept
parameter_list|(
name|String
name|section
parameter_list|)
block|{
name|Set
argument_list|<
name|ConfigKey
argument_list|>
name|entries
init|=
name|getEntriesFromSection
argument_list|(
name|oldConfig
argument_list|,
name|section
argument_list|)
decl_stmt|;
name|entries
operator|.
name|addAll
argument_list|(
name|getEntriesFromSection
argument_list|(
name|newConfig
argument_list|,
name|section
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|createUpdate
argument_list|(
name|entries
argument_list|,
name|UpdateResult
operator|.
name|APPLIED
argument_list|)
return|;
block|}
DECL|method|reject (ConfigKey entry)
specifier|public
name|Update
name|reject
parameter_list|(
name|ConfigKey
name|entry
parameter_list|)
block|{
return|return
name|reject
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|entry
argument_list|)
argument_list|)
return|;
block|}
DECL|method|reject (Set<ConfigKey> entries)
specifier|public
name|Update
name|reject
parameter_list|(
name|Set
argument_list|<
name|ConfigKey
argument_list|>
name|entries
parameter_list|)
block|{
return|return
name|createUpdate
argument_list|(
name|entries
argument_list|,
name|UpdateResult
operator|.
name|REJECTED
argument_list|)
return|;
block|}
DECL|method|getEntriesFromSection (Config config, String section)
specifier|private
specifier|static
name|Set
argument_list|<
name|ConfigKey
argument_list|>
name|getEntriesFromSection
parameter_list|(
name|Config
name|config
parameter_list|,
name|String
name|section
parameter_list|)
block|{
name|Set
argument_list|<
name|ConfigKey
argument_list|>
name|res
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|config
operator|.
name|getNames
argument_list|(
name|section
argument_list|,
literal|true
argument_list|)
control|)
block|{
name|res
operator|.
name|add
argument_list|(
name|ConfigKey
operator|.
name|create
argument_list|(
name|section
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|sub
range|:
name|config
operator|.
name|getSubsections
argument_list|(
name|section
argument_list|)
control|)
block|{
for|for
control|(
name|String
name|name
range|:
name|config
operator|.
name|getNames
argument_list|(
name|section
argument_list|,
name|sub
argument_list|,
literal|true
argument_list|)
control|)
block|{
name|res
operator|.
name|add
argument_list|(
name|ConfigKey
operator|.
name|create
argument_list|(
name|section
argument_list|,
name|sub
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|res
return|;
block|}
DECL|method|createUpdate (Set<ConfigKey> entries, UpdateResult updateResult)
specifier|private
name|Update
name|createUpdate
parameter_list|(
name|Set
argument_list|<
name|ConfigKey
argument_list|>
name|entries
parameter_list|,
name|UpdateResult
name|updateResult
parameter_list|)
block|{
name|Update
name|update
init|=
operator|new
name|Update
argument_list|(
name|updateResult
argument_list|)
decl_stmt|;
name|entries
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|this
operator|::
name|isValueUpdated
argument_list|)
operator|.
name|forEach
argument_list|(
name|key
lambda|->
block|{
name|update
operator|.
name|addConfigUpdate
argument_list|(
operator|new
name|ConfigUpdateEntry
argument_list|(
name|key
argument_list|,
name|oldConfig
operator|.
name|getString
argument_list|(
name|key
operator|.
name|section
argument_list|()
argument_list|,
name|key
operator|.
name|subsection
argument_list|()
argument_list|,
name|key
operator|.
name|name
argument_list|()
argument_list|)
argument_list|,
name|newConfig
operator|.
name|getString
argument_list|(
name|key
operator|.
name|section
argument_list|()
argument_list|,
name|key
operator|.
name|subsection
argument_list|()
argument_list|,
name|key
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
return|return
name|update
return|;
block|}
DECL|method|isSectionUpdated (String section)
specifier|public
name|boolean
name|isSectionUpdated
parameter_list|(
name|String
name|section
parameter_list|)
block|{
name|Set
argument_list|<
name|ConfigKey
argument_list|>
name|entries
init|=
name|getEntriesFromSection
argument_list|(
name|oldConfig
argument_list|,
name|section
argument_list|)
decl_stmt|;
name|entries
operator|.
name|addAll
argument_list|(
name|getEntriesFromSection
argument_list|(
name|newConfig
argument_list|,
name|section
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|isEntriesUpdated
argument_list|(
name|entries
argument_list|)
return|;
block|}
DECL|method|isValueUpdated (String section, String subsection, String name)
specifier|public
name|boolean
name|isValueUpdated
parameter_list|(
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|!
name|Objects
operator|.
name|equals
argument_list|(
name|oldConfig
operator|.
name|getString
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|)
argument_list|,
name|newConfig
operator|.
name|getString
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isValueUpdated (ConfigKey key)
specifier|public
name|boolean
name|isValueUpdated
parameter_list|(
name|ConfigKey
name|key
parameter_list|)
block|{
return|return
name|isValueUpdated
argument_list|(
name|key
operator|.
name|section
argument_list|()
argument_list|,
name|key
operator|.
name|subsection
argument_list|()
argument_list|,
name|key
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
DECL|method|isValueUpdated (String section, String name)
specifier|public
name|boolean
name|isValueUpdated
parameter_list|(
name|String
name|section
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|isValueUpdated
argument_list|(
name|section
argument_list|,
literal|null
argument_list|,
name|name
argument_list|)
return|;
block|}
DECL|method|isEntriesUpdated (Set<ConfigKey> entries)
specifier|public
name|boolean
name|isEntriesUpdated
parameter_list|(
name|Set
argument_list|<
name|ConfigKey
argument_list|>
name|entries
parameter_list|)
block|{
for|for
control|(
name|ConfigKey
name|entry
range|:
name|entries
control|)
block|{
if|if
condition|(
name|isValueUpdated
argument_list|(
name|entry
operator|.
name|section
argument_list|()
argument_list|,
name|entry
operator|.
name|subsection
argument_list|()
argument_list|,
name|entry
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|enum|UpdateResult
specifier|public
enum|enum
name|UpdateResult
block|{
DECL|enumConstant|APPLIED
name|APPLIED
block|,
DECL|enumConstant|REJECTED
name|REJECTED
block|;
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|StringUtils
operator|.
name|capitalize
argument_list|(
name|name
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|/**    * One Accepted/Rejected Update have one or more config updates (ConfigUpdateEntry) tied to it.    */
DECL|class|Update
specifier|public
specifier|static
class|class
name|Update
block|{
DECL|field|result
specifier|private
name|UpdateResult
name|result
decl_stmt|;
DECL|field|configUpdates
specifier|private
specifier|final
name|Set
argument_list|<
name|ConfigUpdateEntry
argument_list|>
name|configUpdates
decl_stmt|;
DECL|method|Update (UpdateResult result)
specifier|public
name|Update
parameter_list|(
name|UpdateResult
name|result
parameter_list|)
block|{
name|this
operator|.
name|configUpdates
operator|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|result
operator|=
name|result
expr_stmt|;
block|}
DECL|method|getResult ()
specifier|public
name|UpdateResult
name|getResult
parameter_list|()
block|{
return|return
name|result
return|;
block|}
DECL|method|getConfigUpdates ()
specifier|public
name|List
argument_list|<
name|ConfigUpdateEntry
argument_list|>
name|getConfigUpdates
parameter_list|()
block|{
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|configUpdates
argument_list|)
return|;
block|}
DECL|method|addConfigUpdate (ConfigUpdateEntry entry)
specifier|public
name|void
name|addConfigUpdate
parameter_list|(
name|ConfigUpdateEntry
name|entry
parameter_list|)
block|{
name|this
operator|.
name|configUpdates
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
block|}
DECL|enum|ConfigEntryType
specifier|public
enum|enum
name|ConfigEntryType
block|{
DECL|enumConstant|ADDED
name|ADDED
block|,
DECL|enumConstant|REMOVED
name|REMOVED
block|,
DECL|enumConstant|MODIFIED
name|MODIFIED
block|,
DECL|enumConstant|UNMODIFIED
name|UNMODIFIED
block|}
DECL|class|ConfigUpdateEntry
specifier|public
specifier|static
class|class
name|ConfigUpdateEntry
block|{
DECL|field|key
specifier|public
specifier|final
name|ConfigKey
name|key
decl_stmt|;
DECL|field|oldVal
specifier|public
specifier|final
name|String
name|oldVal
decl_stmt|;
DECL|field|newVal
specifier|public
specifier|final
name|String
name|newVal
decl_stmt|;
DECL|method|ConfigUpdateEntry (ConfigKey key, String oldVal, String newVal)
specifier|public
name|ConfigUpdateEntry
parameter_list|(
name|ConfigKey
name|key
parameter_list|,
name|String
name|oldVal
parameter_list|,
name|String
name|newVal
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|oldVal
operator|=
name|oldVal
expr_stmt|;
name|this
operator|.
name|newVal
operator|=
name|newVal
expr_stmt|;
block|}
comment|/** Note: The toString() is used to format the output from @see ReloadConfig. */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
switch|switch
condition|(
name|getUpdateType
argument_list|()
condition|)
block|{
case|case
name|ADDED
case|:
return|return
name|String
operator|.
name|format
argument_list|(
literal|"+ %s = %s"
argument_list|,
name|key
argument_list|,
name|newVal
argument_list|)
return|;
case|case
name|MODIFIED
case|:
return|return
name|String
operator|.
name|format
argument_list|(
literal|"* %s = [%s => %s]"
argument_list|,
name|key
argument_list|,
name|oldVal
argument_list|,
name|newVal
argument_list|)
return|;
case|case
name|REMOVED
case|:
return|return
name|String
operator|.
name|format
argument_list|(
literal|"- %s = %s"
argument_list|,
name|key
argument_list|,
name|oldVal
argument_list|)
return|;
case|case
name|UNMODIFIED
case|:
return|return
name|String
operator|.
name|format
argument_list|(
literal|"  %s = %s"
argument_list|,
name|key
argument_list|,
name|newVal
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unexpected UpdateType: "
operator|+
name|getUpdateType
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|getUpdateType ()
specifier|public
name|ConfigEntryType
name|getUpdateType
parameter_list|()
block|{
if|if
condition|(
name|oldVal
operator|==
literal|null
operator|&&
name|newVal
operator|!=
literal|null
condition|)
block|{
return|return
name|ConfigEntryType
operator|.
name|ADDED
return|;
block|}
if|if
condition|(
name|oldVal
operator|!=
literal|null
operator|&&
name|newVal
operator|==
literal|null
condition|)
block|{
return|return
name|ConfigEntryType
operator|.
name|REMOVED
return|;
block|}
if|if
condition|(
name|Objects
operator|.
name|equals
argument_list|(
name|oldVal
argument_list|,
name|newVal
argument_list|)
condition|)
block|{
return|return
name|ConfigEntryType
operator|.
name|UNMODIFIED
return|;
block|}
return|return
name|ConfigEntryType
operator|.
name|MODIFIED
return|;
block|}
block|}
block|}
end_class

end_unit

