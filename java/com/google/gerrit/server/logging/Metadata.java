begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.logging
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|logging
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
import|;
end_import

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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|Nullable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_comment
comment|/** Metadata that is provided to {@link PerformanceLogger}s as context for performance records. */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|Metadata
specifier|public
specifier|abstract
class|class
name|Metadata
block|{
comment|// The numeric ID of an account.
DECL|method|accountId ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Integer
argument_list|>
name|accountId
parameter_list|()
function_decl|;
comment|// The type of an action (ACCOUNT_UPDATE, CHANGE_UPDATE, GROUP_UPDATE, INDEX_QUERY,
comment|// PLUGIN_UPDATE).
DECL|method|actionType ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|actionType
parameter_list|()
function_decl|;
comment|// An authentication domain name.
DECL|method|authDomainName ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|authDomainName
parameter_list|()
function_decl|;
comment|// The name of a branch.
DECL|method|branchName ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|branchName
parameter_list|()
function_decl|;
comment|// Key of an entity in a cache.
DECL|method|cacheKey ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|cacheKey
parameter_list|()
function_decl|;
comment|// The name of a cache.
DECL|method|cacheName ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|cacheName
parameter_list|()
function_decl|;
comment|// The name of the implementation class.
DECL|method|className ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|className
parameter_list|()
function_decl|;
comment|// The numeric ID of a change.
DECL|method|changeId ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Integer
argument_list|>
name|changeId
parameter_list|()
function_decl|;
comment|// The type of change ID which the user used to identify a change (e.g. numeric ID, triplet etc.).
DECL|method|changeIdType ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|changeIdType
parameter_list|()
function_decl|;
comment|// The type of an event.
DECL|method|eventType ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|eventType
parameter_list|()
function_decl|;
comment|// The value of the @Export annotation which was used to register a plugin extension.
DECL|method|exportValue ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|exportValue
parameter_list|()
function_decl|;
comment|// Path of a file in a repository.
DECL|method|filePath ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|filePath
parameter_list|()
function_decl|;
comment|// Garbage collector name.
DECL|method|garbageCollectorName ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|garbageCollectorName
parameter_list|()
function_decl|;
comment|// Git operation (CLONE, FETCH).
DECL|method|gitOperation ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|gitOperation
parameter_list|()
function_decl|;
comment|// The numeric ID of an internal group.
DECL|method|groupId ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Integer
argument_list|>
name|groupId
parameter_list|()
function_decl|;
comment|// The name of a group.
DECL|method|groupName ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|groupName
parameter_list|()
function_decl|;
comment|// The UUID of a group.
DECL|method|groupUuid ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|groupUuid
parameter_list|()
function_decl|;
comment|// HTTP status response code.
DECL|method|httpStatus ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Integer
argument_list|>
name|httpStatus
parameter_list|()
function_decl|;
comment|// The name of a secondary index.
DECL|method|indexName ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|indexName
parameter_list|()
function_decl|;
comment|// The version of a secondary index.
DECL|method|indexVersion ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Integer
argument_list|>
name|indexVersion
parameter_list|()
function_decl|;
comment|// The name of the implementation method.
DECL|method|methodName ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|methodName
parameter_list|()
function_decl|;
comment|// Boolean: one or more
DECL|method|multiple ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Boolean
argument_list|>
name|multiple
parameter_list|()
function_decl|;
comment|// Path of a metadata file in NoteDb.
DECL|method|noteDbFilePath ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|noteDbFilePath
parameter_list|()
function_decl|;
comment|// Name of a metadata ref in NoteDb.
DECL|method|noteDbRefName ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|noteDbRefName
parameter_list|()
function_decl|;
comment|// Type of a sequence in NoteDb (ACCOUNTS, CHANGES, GROUPS).
DECL|method|noteDbSequenceType ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|noteDbSequenceType
parameter_list|()
function_decl|;
comment|// Name of a "table" in NoteDb (if set, always CHANGES).
DECL|method|noteDbTable ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|noteDbTable
parameter_list|()
function_decl|;
comment|// The ID of a patch set.
DECL|method|patchSetId ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Integer
argument_list|>
name|patchSetId
parameter_list|()
function_decl|;
comment|// Plugin metadata that doesn't fit into any other category.
DECL|method|pluginMetadata ()
specifier|public
specifier|abstract
name|ImmutableList
argument_list|<
name|PluginMetadata
argument_list|>
name|pluginMetadata
parameter_list|()
function_decl|;
comment|// The name of a plugin.
DECL|method|pluginName ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|pluginName
parameter_list|()
function_decl|;
comment|// The name of a Gerrit project (aka Git repository).
DECL|method|projectName ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|projectName
parameter_list|()
function_decl|;
comment|// The type of a Git push to Gerrit (CREATE_REPLACE, NORMAL, AUTOCLOSE).
DECL|method|pushType ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|pushType
parameter_list|()
function_decl|;
comment|// The number of resources that is processed.
DECL|method|resourceCount ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Integer
argument_list|>
name|resourceCount
parameter_list|()
function_decl|;
comment|// The name of a REST view.
DECL|method|restViewName ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|restViewName
parameter_list|()
function_decl|;
comment|// The SHA1 of Git commit.
DECL|method|revision ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|revision
parameter_list|()
function_decl|;
comment|// The username of an account.
DECL|method|username ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|username
parameter_list|()
function_decl|;
DECL|method|builder ()
specifier|public
specifier|static
name|Metadata
operator|.
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_Metadata
operator|.
name|Builder
argument_list|()
return|;
block|}
DECL|method|empty ()
specifier|public
specifier|static
name|Metadata
name|empty
parameter_list|()
block|{
return|return
name|builder
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|AutoValue
operator|.
name|Builder
DECL|class|Builder
specifier|public
specifier|abstract
specifier|static
class|class
name|Builder
block|{
DECL|method|accountId (int accountId)
specifier|public
specifier|abstract
name|Builder
name|accountId
parameter_list|(
name|int
name|accountId
parameter_list|)
function_decl|;
DECL|method|actionType (@ullable String actionType)
specifier|public
specifier|abstract
name|Builder
name|actionType
parameter_list|(
annotation|@
name|Nullable
name|String
name|actionType
parameter_list|)
function_decl|;
DECL|method|authDomainName (@ullable String authDomainName)
specifier|public
specifier|abstract
name|Builder
name|authDomainName
parameter_list|(
annotation|@
name|Nullable
name|String
name|authDomainName
parameter_list|)
function_decl|;
DECL|method|branchName (@ullable String branchName)
specifier|public
specifier|abstract
name|Builder
name|branchName
parameter_list|(
annotation|@
name|Nullable
name|String
name|branchName
parameter_list|)
function_decl|;
DECL|method|cacheKey (@ullable String cacheKey)
specifier|public
specifier|abstract
name|Builder
name|cacheKey
parameter_list|(
annotation|@
name|Nullable
name|String
name|cacheKey
parameter_list|)
function_decl|;
DECL|method|cacheName (@ullable String cacheName)
specifier|public
specifier|abstract
name|Builder
name|cacheName
parameter_list|(
annotation|@
name|Nullable
name|String
name|cacheName
parameter_list|)
function_decl|;
DECL|method|className (@ullable String className)
specifier|public
specifier|abstract
name|Builder
name|className
parameter_list|(
annotation|@
name|Nullable
name|String
name|className
parameter_list|)
function_decl|;
DECL|method|changeId (int changeId)
specifier|public
specifier|abstract
name|Builder
name|changeId
parameter_list|(
name|int
name|changeId
parameter_list|)
function_decl|;
DECL|method|changeIdType (@ullable String changeIdType)
specifier|public
specifier|abstract
name|Builder
name|changeIdType
parameter_list|(
annotation|@
name|Nullable
name|String
name|changeIdType
parameter_list|)
function_decl|;
DECL|method|eventType (@ullable String eventType)
specifier|public
specifier|abstract
name|Builder
name|eventType
parameter_list|(
annotation|@
name|Nullable
name|String
name|eventType
parameter_list|)
function_decl|;
DECL|method|exportValue (@ullable String exportValue)
specifier|public
specifier|abstract
name|Builder
name|exportValue
parameter_list|(
annotation|@
name|Nullable
name|String
name|exportValue
parameter_list|)
function_decl|;
DECL|method|filePath (@ullable String filePath)
specifier|public
specifier|abstract
name|Builder
name|filePath
parameter_list|(
annotation|@
name|Nullable
name|String
name|filePath
parameter_list|)
function_decl|;
DECL|method|garbageCollectorName (@ullable String garbageCollectorName)
specifier|public
specifier|abstract
name|Builder
name|garbageCollectorName
parameter_list|(
annotation|@
name|Nullable
name|String
name|garbageCollectorName
parameter_list|)
function_decl|;
DECL|method|gitOperation (@ullable String gitOperation)
specifier|public
specifier|abstract
name|Builder
name|gitOperation
parameter_list|(
annotation|@
name|Nullable
name|String
name|gitOperation
parameter_list|)
function_decl|;
DECL|method|groupId (int groupId)
specifier|public
specifier|abstract
name|Builder
name|groupId
parameter_list|(
name|int
name|groupId
parameter_list|)
function_decl|;
DECL|method|groupName (@ullable String groupName)
specifier|public
specifier|abstract
name|Builder
name|groupName
parameter_list|(
annotation|@
name|Nullable
name|String
name|groupName
parameter_list|)
function_decl|;
DECL|method|groupUuid (@ullable String groupUuid)
specifier|public
specifier|abstract
name|Builder
name|groupUuid
parameter_list|(
annotation|@
name|Nullable
name|String
name|groupUuid
parameter_list|)
function_decl|;
DECL|method|httpStatus (int httpStatus)
specifier|public
specifier|abstract
name|Builder
name|httpStatus
parameter_list|(
name|int
name|httpStatus
parameter_list|)
function_decl|;
DECL|method|indexName (@ullable String indexName)
specifier|public
specifier|abstract
name|Builder
name|indexName
parameter_list|(
annotation|@
name|Nullable
name|String
name|indexName
parameter_list|)
function_decl|;
DECL|method|indexVersion (int indexVersion)
specifier|public
specifier|abstract
name|Builder
name|indexVersion
parameter_list|(
name|int
name|indexVersion
parameter_list|)
function_decl|;
DECL|method|methodName (@ullable String methodName)
specifier|public
specifier|abstract
name|Builder
name|methodName
parameter_list|(
annotation|@
name|Nullable
name|String
name|methodName
parameter_list|)
function_decl|;
DECL|method|multiple (boolean multiple)
specifier|public
specifier|abstract
name|Builder
name|multiple
parameter_list|(
name|boolean
name|multiple
parameter_list|)
function_decl|;
DECL|method|noteDbFilePath (@ullable String noteDbFilePath)
specifier|public
specifier|abstract
name|Builder
name|noteDbFilePath
parameter_list|(
annotation|@
name|Nullable
name|String
name|noteDbFilePath
parameter_list|)
function_decl|;
DECL|method|noteDbRefName (@ullable String noteDbRefName)
specifier|public
specifier|abstract
name|Builder
name|noteDbRefName
parameter_list|(
annotation|@
name|Nullable
name|String
name|noteDbRefName
parameter_list|)
function_decl|;
DECL|method|noteDbSequenceType (@ullable String noteDbSequenceType)
specifier|public
specifier|abstract
name|Builder
name|noteDbSequenceType
parameter_list|(
annotation|@
name|Nullable
name|String
name|noteDbSequenceType
parameter_list|)
function_decl|;
DECL|method|noteDbTable (@ullable String noteDbTable)
specifier|public
specifier|abstract
name|Builder
name|noteDbTable
parameter_list|(
annotation|@
name|Nullable
name|String
name|noteDbTable
parameter_list|)
function_decl|;
DECL|method|patchSetId (int patchSetId)
specifier|public
specifier|abstract
name|Builder
name|patchSetId
parameter_list|(
name|int
name|patchSetId
parameter_list|)
function_decl|;
DECL|method|pluginMetadataBuilder ()
specifier|abstract
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|PluginMetadata
argument_list|>
name|pluginMetadataBuilder
parameter_list|()
function_decl|;
DECL|method|addPluginMetadata (PluginMetadata pluginMetadata)
specifier|public
name|Builder
name|addPluginMetadata
parameter_list|(
name|PluginMetadata
name|pluginMetadata
parameter_list|)
block|{
name|pluginMetadataBuilder
argument_list|()
operator|.
name|add
argument_list|(
name|pluginMetadata
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|pluginName (@ullable String pluginName)
specifier|public
specifier|abstract
name|Builder
name|pluginName
parameter_list|(
annotation|@
name|Nullable
name|String
name|pluginName
parameter_list|)
function_decl|;
DECL|method|projectName (@ullable String projectName)
specifier|public
specifier|abstract
name|Builder
name|projectName
parameter_list|(
annotation|@
name|Nullable
name|String
name|projectName
parameter_list|)
function_decl|;
DECL|method|pushType (@ullable String pushType)
specifier|public
specifier|abstract
name|Builder
name|pushType
parameter_list|(
annotation|@
name|Nullable
name|String
name|pushType
parameter_list|)
function_decl|;
DECL|method|resourceCount (int resourceCount)
specifier|public
specifier|abstract
name|Builder
name|resourceCount
parameter_list|(
name|int
name|resourceCount
parameter_list|)
function_decl|;
DECL|method|restViewName (@ullable String restViewName)
specifier|public
specifier|abstract
name|Builder
name|restViewName
parameter_list|(
annotation|@
name|Nullable
name|String
name|restViewName
parameter_list|)
function_decl|;
DECL|method|revision (@ullable String revision)
specifier|public
specifier|abstract
name|Builder
name|revision
parameter_list|(
annotation|@
name|Nullable
name|String
name|revision
parameter_list|)
function_decl|;
DECL|method|username (@ullable String username)
specifier|public
specifier|abstract
name|Builder
name|username
parameter_list|(
annotation|@
name|Nullable
name|String
name|username
parameter_list|)
function_decl|;
DECL|method|build ()
specifier|public
specifier|abstract
name|Metadata
name|build
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

