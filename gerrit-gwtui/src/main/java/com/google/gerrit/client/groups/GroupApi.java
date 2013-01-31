begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.groups
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|groups
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
name|VoidResult
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
name|NativeList
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
name|RestApi
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
name|reviewdb
operator|.
name|client
operator|.
name|Account
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGroup
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGroupIncludeByUuid
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|JavaScriptObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|AsyncCallback
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

begin_comment
comment|/**  * A collection of static methods which work on the Gerrit REST API for specific  * groups.  */
end_comment

begin_class
DECL|class|GroupApi
specifier|public
class|class
name|GroupApi
block|{
comment|/** Create a new group */
DECL|method|createGroup (String groupName, AsyncCallback<GroupInfo> cb)
specifier|public
specifier|static
name|void
name|createGroup
parameter_list|(
name|String
name|groupName
parameter_list|,
name|AsyncCallback
argument_list|<
name|GroupInfo
argument_list|>
name|cb
parameter_list|)
block|{
name|JavaScriptObject
name|in
init|=
name|JavaScriptObject
operator|.
name|createObject
argument_list|()
decl_stmt|;
operator|new
name|RestApi
argument_list|(
literal|"/groups/"
argument_list|)
operator|.
name|id
argument_list|(
name|groupName
argument_list|)
operator|.
name|ifNoneMatch
argument_list|()
operator|.
name|put
argument_list|(
name|in
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Rename a group */
DECL|method|renameGroup (AccountGroup.UUID group, String newName, AsyncCallback<VoidResult> cb)
specifier|public
specifier|static
name|void
name|renameGroup
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|group
parameter_list|,
name|String
name|newName
parameter_list|,
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|cb
parameter_list|)
block|{
name|GroupInput
name|in
init|=
name|GroupInput
operator|.
name|create
argument_list|()
decl_stmt|;
name|in
operator|.
name|name
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|group
argument_list|(
name|group
argument_list|)
operator|.
name|view
argument_list|(
literal|"name"
argument_list|)
operator|.
name|put
argument_list|(
name|in
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Set description for a group */
DECL|method|setGroupDescription (AccountGroup.UUID group, String description, AsyncCallback<VoidResult> cb)
specifier|public
specifier|static
name|void
name|setGroupDescription
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|group
parameter_list|,
name|String
name|description
parameter_list|,
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|cb
parameter_list|)
block|{
name|RestApi
name|call
init|=
name|group
argument_list|(
name|group
argument_list|)
operator|.
name|view
argument_list|(
literal|"description"
argument_list|)
decl_stmt|;
if|if
condition|(
name|description
operator|!=
literal|null
operator|&&
operator|!
name|description
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|GroupInput
name|in
init|=
name|GroupInput
operator|.
name|create
argument_list|()
decl_stmt|;
name|in
operator|.
name|description
argument_list|(
name|description
argument_list|)
expr_stmt|;
name|call
operator|.
name|put
argument_list|(
name|in
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|call
operator|.
name|delete
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Set owner for a group */
DECL|method|setGroupOwner (AccountGroup.UUID group, String owner, AsyncCallback<VoidResult> cb)
specifier|public
specifier|static
name|void
name|setGroupOwner
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|group
parameter_list|,
name|String
name|owner
parameter_list|,
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|cb
parameter_list|)
block|{
name|GroupInput
name|in
init|=
name|GroupInput
operator|.
name|create
argument_list|()
decl_stmt|;
name|in
operator|.
name|owner
argument_list|(
name|owner
argument_list|)
expr_stmt|;
name|group
argument_list|(
name|group
argument_list|)
operator|.
name|view
argument_list|(
literal|"owner"
argument_list|)
operator|.
name|put
argument_list|(
name|in
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Add member to a group. */
DECL|method|addMember (AccountGroup.UUID group, String member, AsyncCallback<MemberInfo> cb)
specifier|public
specifier|static
name|void
name|addMember
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|group
parameter_list|,
name|String
name|member
parameter_list|,
name|AsyncCallback
argument_list|<
name|MemberInfo
argument_list|>
name|cb
parameter_list|)
block|{
name|members
argument_list|(
name|group
argument_list|)
operator|.
name|id
argument_list|(
name|member
argument_list|)
operator|.
name|put
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Add members to a group. */
DECL|method|addMembers (AccountGroup.UUID group, Set<String> members, final AsyncCallback<NativeList<MemberInfo>> cb)
specifier|public
specifier|static
name|void
name|addMembers
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|group
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|members
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|NativeList
argument_list|<
name|MemberInfo
argument_list|>
argument_list|>
name|cb
parameter_list|)
block|{
if|if
condition|(
name|members
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|addMember
argument_list|(
name|group
argument_list|,
name|members
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|,
operator|new
name|AsyncCallback
argument_list|<
name|MemberInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|MemberInfo
name|result
parameter_list|)
block|{
name|cb
operator|.
name|onSuccess
argument_list|(
name|NativeList
operator|.
name|of
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{
name|cb
operator|.
name|onFailure
argument_list|(
name|caught
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|MemberInput
name|input
init|=
name|MemberInput
operator|.
name|create
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|member
range|:
name|members
control|)
block|{
name|input
operator|.
name|add_member
argument_list|(
name|member
argument_list|)
expr_stmt|;
block|}
name|members
argument_list|(
name|group
argument_list|)
operator|.
name|post
argument_list|(
name|input
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Remove members from a group. */
DECL|method|removeMembers (AccountGroup.UUID group, Set<Account.Id> ids, final AsyncCallback<VoidResult> cb)
specifier|public
specifier|static
name|void
name|removeMembers
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|group
parameter_list|,
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|ids
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|cb
parameter_list|)
block|{
if|if
condition|(
name|ids
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|Account
operator|.
name|Id
name|u
init|=
name|ids
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|members
argument_list|(
name|group
argument_list|)
operator|.
name|id
argument_list|(
name|u
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|delete
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|MemberInput
name|in
init|=
name|MemberInput
operator|.
name|create
argument_list|()
decl_stmt|;
for|for
control|(
name|Account
operator|.
name|Id
name|u
range|:
name|ids
control|)
block|{
name|in
operator|.
name|add_member
argument_list|(
name|u
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|group
argument_list|(
name|group
argument_list|)
operator|.
name|view
argument_list|(
literal|"members.delete"
argument_list|)
operator|.
name|post
argument_list|(
name|in
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Include a group into a group. */
DECL|method|addIncludedGroup (AccountGroup.UUID group, String include, AsyncCallback<GroupInfo> cb)
specifier|public
specifier|static
name|void
name|addIncludedGroup
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|group
parameter_list|,
name|String
name|include
parameter_list|,
name|AsyncCallback
argument_list|<
name|GroupInfo
argument_list|>
name|cb
parameter_list|)
block|{
name|groups
argument_list|(
name|group
argument_list|)
operator|.
name|id
argument_list|(
name|include
argument_list|)
operator|.
name|put
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
comment|/** Include groups into a group. */
DECL|method|addIncludedGroups (AccountGroup.UUID group, Set<String> includedGroups, final AsyncCallback<NativeList<GroupInfo>> cb)
specifier|public
specifier|static
name|void
name|addIncludedGroups
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|group
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|includedGroups
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|NativeList
argument_list|<
name|GroupInfo
argument_list|>
argument_list|>
name|cb
parameter_list|)
block|{
if|if
condition|(
name|includedGroups
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|addIncludedGroup
argument_list|(
name|group
argument_list|,
name|includedGroups
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|,
operator|new
name|AsyncCallback
argument_list|<
name|GroupInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|GroupInfo
name|result
parameter_list|)
block|{
name|cb
operator|.
name|onSuccess
argument_list|(
name|NativeList
operator|.
name|of
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{
name|cb
operator|.
name|onFailure
argument_list|(
name|caught
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|IncludedGroupInput
name|input
init|=
name|IncludedGroupInput
operator|.
name|create
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|includedGroup
range|:
name|includedGroups
control|)
block|{
name|input
operator|.
name|add_group
argument_list|(
name|includedGroup
argument_list|)
expr_stmt|;
block|}
name|groups
argument_list|(
name|group
argument_list|)
operator|.
name|post
argument_list|(
name|input
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Remove included groups from a group. */
DECL|method|removeIncludedGroups (AccountGroup.UUID group, Set<AccountGroupIncludeByUuid.Key> ids, final AsyncCallback<VoidResult> cb)
specifier|public
specifier|static
name|void
name|removeIncludedGroups
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|group
parameter_list|,
name|Set
argument_list|<
name|AccountGroupIncludeByUuid
operator|.
name|Key
argument_list|>
name|ids
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|VoidResult
argument_list|>
name|cb
parameter_list|)
block|{
if|if
condition|(
name|ids
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|AccountGroupIncludeByUuid
operator|.
name|Key
name|g
init|=
name|ids
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|groups
argument_list|(
name|group
argument_list|)
operator|.
name|id
argument_list|(
name|g
operator|.
name|getIncludeUUID
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|delete
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|IncludedGroupInput
name|in
init|=
name|IncludedGroupInput
operator|.
name|create
argument_list|()
decl_stmt|;
for|for
control|(
name|AccountGroupIncludeByUuid
operator|.
name|Key
name|g
range|:
name|ids
control|)
block|{
name|in
operator|.
name|add_group
argument_list|(
name|g
operator|.
name|getIncludeUUID
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|group
argument_list|(
name|group
argument_list|)
operator|.
name|view
argument_list|(
literal|"groups.delete"
argument_list|)
operator|.
name|post
argument_list|(
name|in
argument_list|,
name|cb
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|members (AccountGroup.UUID group)
specifier|private
specifier|static
name|RestApi
name|members
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|group
parameter_list|)
block|{
return|return
name|group
argument_list|(
name|group
argument_list|)
operator|.
name|view
argument_list|(
literal|"members"
argument_list|)
return|;
block|}
DECL|method|groups (AccountGroup.UUID group)
specifier|private
specifier|static
name|RestApi
name|groups
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|group
parameter_list|)
block|{
return|return
name|group
argument_list|(
name|group
argument_list|)
operator|.
name|view
argument_list|(
literal|"groups"
argument_list|)
return|;
block|}
DECL|method|group (AccountGroup.UUID group)
specifier|private
specifier|static
name|RestApi
name|group
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|group
parameter_list|)
block|{
return|return
operator|new
name|RestApi
argument_list|(
literal|"/groups/"
argument_list|)
operator|.
name|id
argument_list|(
name|group
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|class|GroupInput
specifier|private
specifier|static
class|class
name|GroupInput
extends|extends
name|JavaScriptObject
block|{
DECL|method|description (String d)
specifier|final
specifier|native
name|void
name|description
parameter_list|(
name|String
name|d
parameter_list|)
comment|/*-{ if(d)this.description=d; }-*/
function_decl|;
DECL|method|name (String n)
specifier|final
specifier|native
name|void
name|name
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ if(n)this.name=n; }-*/
function_decl|;
DECL|method|owner (String o)
specifier|final
specifier|native
name|void
name|owner
parameter_list|(
name|String
name|o
parameter_list|)
comment|/*-{ if(o)this.owner=o; }-*/
function_decl|;
DECL|method|create ()
specifier|static
name|GroupInput
name|create
parameter_list|()
block|{
return|return
operator|(
name|GroupInput
operator|)
name|createObject
argument_list|()
return|;
block|}
DECL|method|GroupInput ()
specifier|protected
name|GroupInput
parameter_list|()
block|{     }
block|}
DECL|class|MemberInput
specifier|private
specifier|static
class|class
name|MemberInput
extends|extends
name|JavaScriptObject
block|{
DECL|method|init ()
specifier|final
specifier|native
name|void
name|init
parameter_list|()
comment|/*-{ this.members = []; }-*/
function_decl|;
DECL|method|add_member (String n)
specifier|final
specifier|native
name|void
name|add_member
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ this.members.push(n); }-*/
function_decl|;
DECL|method|create ()
specifier|static
name|MemberInput
name|create
parameter_list|()
block|{
name|MemberInput
name|m
init|=
operator|(
name|MemberInput
operator|)
name|createObject
argument_list|()
decl_stmt|;
name|m
operator|.
name|init
argument_list|()
expr_stmt|;
return|return
name|m
return|;
block|}
DECL|method|MemberInput ()
specifier|protected
name|MemberInput
parameter_list|()
block|{     }
block|}
DECL|class|IncludedGroupInput
specifier|private
specifier|static
class|class
name|IncludedGroupInput
extends|extends
name|JavaScriptObject
block|{
DECL|method|init ()
specifier|final
specifier|native
name|void
name|init
parameter_list|()
comment|/*-{ this.groups = []; }-*/
function_decl|;
DECL|method|add_group (String n)
specifier|final
specifier|native
name|void
name|add_group
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ this.groups.push(n); }-*/
function_decl|;
DECL|method|create ()
specifier|static
name|IncludedGroupInput
name|create
parameter_list|()
block|{
name|IncludedGroupInput
name|g
init|=
operator|(
name|IncludedGroupInput
operator|)
name|createObject
argument_list|()
decl_stmt|;
name|g
operator|.
name|init
argument_list|()
expr_stmt|;
return|return
name|g
return|;
block|}
DECL|method|IncludedGroupInput ()
specifier|protected
name|IncludedGroupInput
parameter_list|()
block|{     }
block|}
block|}
end_class

end_unit

