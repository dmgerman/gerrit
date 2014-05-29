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
name|AuthException
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
name|MethodNotAllowedException
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
name|extensions
operator|.
name|restapi
operator|.
name|TopLevelResource
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
name|AccountGroupMember
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
name|server
operator|.
name|account
operator|.
name|AccountsCollection
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
name|AddMembers
operator|.
name|PutMember
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

begin_class
annotation|@
name|Singleton
DECL|class|MembersCollection
specifier|public
class|class
name|MembersCollection
implements|implements
name|ChildCollection
argument_list|<
name|GroupResource
argument_list|,
name|MemberResource
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
name|MemberResource
argument_list|>
argument_list|>
name|views
decl_stmt|;
DECL|field|list
specifier|private
specifier|final
name|Provider
argument_list|<
name|ListMembers
argument_list|>
name|list
decl_stmt|;
DECL|field|accounts
specifier|private
specifier|final
name|AccountsCollection
name|accounts
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|put
specifier|private
specifier|final
name|AddMembers
name|put
decl_stmt|;
annotation|@
name|Inject
DECL|method|MembersCollection (DynamicMap<RestView<MemberResource>> views, Provider<ListMembers> list, AccountsCollection accounts, Provider<ReviewDb> db, AddMembers put)
name|MembersCollection
parameter_list|(
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|MemberResource
argument_list|>
argument_list|>
name|views
parameter_list|,
name|Provider
argument_list|<
name|ListMembers
argument_list|>
name|list
parameter_list|,
name|AccountsCollection
name|accounts
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|AddMembers
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
name|accounts
operator|=
name|accounts
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
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
throws|throws
name|ResourceNotFoundException
throws|,
name|AuthException
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
name|MemberResource
name|parse
parameter_list|(
name|GroupResource
name|parent
parameter_list|,
name|IdString
name|id
parameter_list|)
throws|throws
name|MethodNotAllowedException
throws|,
name|AuthException
throws|,
name|ResourceNotFoundException
throws|,
name|OrmException
block|{
if|if
condition|(
name|parent
operator|.
name|toAccountGroup
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MethodNotAllowedException
argument_list|()
throw|;
block|}
name|IdentifiedUser
name|user
init|=
name|accounts
operator|.
name|parse
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|,
name|id
argument_list|)
operator|.
name|getUser
argument_list|()
decl_stmt|;
name|AccountGroupMember
operator|.
name|Key
name|key
init|=
operator|new
name|AccountGroupMember
operator|.
name|Key
argument_list|(
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|parent
operator|.
name|toAccountGroup
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|db
operator|.
name|get
argument_list|()
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
operator|&&
name|parent
operator|.
name|getControl
argument_list|()
operator|.
name|canSeeMember
argument_list|(
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|MemberResource
argument_list|(
name|parent
argument_list|,
name|user
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
name|PutMember
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
name|PutMember
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
name|MemberResource
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

