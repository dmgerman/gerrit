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
DECL|package|com.google.gerrit.server.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
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
name|common
operator|.
name|data
operator|.
name|GroupDescription
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
name|extensions
operator|.
name|registration
operator|.
name|DynamicMap
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
name|extensions
operator|.
name|restapi
operator|.
name|AcceptsCreate
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
name|extensions
operator|.
name|restapi
operator|.
name|ChildCollection
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
name|extensions
operator|.
name|restapi
operator|.
name|IdString
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
name|extensions
operator|.
name|restapi
operator|.
name|ResourceNotFoundException
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
name|extensions
operator|.
name|restapi
operator|.
name|RestView
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|server
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
name|server
operator|.
name|group
operator|.
name|AddIncludedGroups
operator|.
name|PutIncludedGroup
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
name|server
operator|.
name|OrmException
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

begin_class
DECL|class|IncludedGroupsCollection
specifier|public
class|class
name|IncludedGroupsCollection
implements|implements
name|ChildCollection
argument_list|<
name|GroupResource
argument_list|,
name|IncludedGroupResource
argument_list|>
implements|,
name|AcceptsCreate
argument_list|<
name|GroupResource
argument_list|>
block|{
DECL|field|views
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|IncludedGroupResource
argument_list|>
argument_list|>
name|views
decl_stmt|;
DECL|field|list
specifier|private
specifier|final
name|Provider
argument_list|<
name|ListIncludedGroups
argument_list|>
name|list
decl_stmt|;
DECL|field|groupsCollection
specifier|private
specifier|final
name|Provider
argument_list|<
name|GroupsCollection
argument_list|>
name|groupsCollection
decl_stmt|;
DECL|field|dbProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|field|put
specifier|private
specifier|final
name|Provider
argument_list|<
name|AddIncludedGroups
argument_list|>
name|put
decl_stmt|;
annotation|@
name|Inject
DECL|method|IncludedGroupsCollection (DynamicMap<RestView<IncludedGroupResource>> views, Provider<ListIncludedGroups> list, Provider<GroupsCollection> groupsCollection, Provider<ReviewDb> dbProvider, Provider<AddIncludedGroups> put)
name|IncludedGroupsCollection
parameter_list|(
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|IncludedGroupResource
argument_list|>
argument_list|>
name|views
parameter_list|,
name|Provider
argument_list|<
name|ListIncludedGroups
argument_list|>
name|list
parameter_list|,
name|Provider
argument_list|<
name|GroupsCollection
argument_list|>
name|groupsCollection
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|Provider
argument_list|<
name|AddIncludedGroups
argument_list|>
name|put
parameter_list|)
block|{
name|this
operator|.
name|views
operator|=
name|views
expr_stmt|;
name|this
operator|.
name|list
operator|=
name|list
expr_stmt|;
name|this
operator|.
name|groupsCollection
operator|=
name|groupsCollection
expr_stmt|;
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
name|this
operator|.
name|put
operator|=
name|put
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|list ()
specifier|public
name|RestView
argument_list|<
name|GroupResource
argument_list|>
name|list
parameter_list|()
block|{
return|return
name|list
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|parse (GroupResource parent, IdString id)
specifier|public
name|IncludedGroupResource
name|parse
parameter_list|(
name|GroupResource
name|parent
parameter_list|,
name|IdString
name|id
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|OrmException
block|{
if|if
condition|(
operator|!
name|parent
operator|.
name|isInternal
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|id
argument_list|)
throw|;
block|}
name|GroupDescription
operator|.
name|Internal
name|p
init|=
operator|(
name|GroupDescription
operator|.
name|Internal
operator|)
name|parent
operator|.
name|getGroup
argument_list|()
decl_stmt|;
name|GroupResource
name|included
init|=
name|groupsCollection
operator|.
name|get
argument_list|()
operator|.
name|parse
argument_list|(
name|id
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|AccountGroupIncludeByUuid
name|in
init|=
name|dbProvider
operator|.
name|get
argument_list|()
operator|.
name|accountGroupIncludesByUuid
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|AccountGroupIncludeByUuid
operator|.
name|Key
argument_list|(
name|p
operator|.
name|getAccountGroup
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|included
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|IncludedGroupResource
argument_list|(
name|included
operator|.
name|getControl
argument_list|()
argument_list|)
return|;
block|}
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|id
argument_list|)
throw|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|create (GroupResource group, IdString id)
specifier|public
name|PutIncludedGroup
name|create
parameter_list|(
name|GroupResource
name|group
parameter_list|,
name|IdString
name|id
parameter_list|)
block|{
return|return
operator|new
name|PutIncludedGroup
argument_list|(
name|put
argument_list|,
name|id
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|views ()
specifier|public
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|IncludedGroupResource
argument_list|>
argument_list|>
name|views
parameter_list|()
block|{
return|return
name|views
return|;
block|}
block|}
end_class

end_unit

