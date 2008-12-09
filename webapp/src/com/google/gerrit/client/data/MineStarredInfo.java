begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|data
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
name|changes
operator|.
name|MineStarredScreen
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
name|Account
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

begin_comment
comment|/** Summary information needed for {@link MineStarredScreen}. */
end_comment

begin_class
DECL|class|MineStarredInfo
specifier|public
class|class
name|MineStarredInfo
block|{
DECL|field|accounts
specifier|protected
name|AccountInfoCache
name|accounts
decl_stmt|;
DECL|field|owner
specifier|protected
name|Account
operator|.
name|Id
name|owner
decl_stmt|;
DECL|field|starred
specifier|protected
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|starred
decl_stmt|;
DECL|method|MineStarredInfo ()
specifier|protected
name|MineStarredInfo
parameter_list|()
block|{   }
DECL|method|MineStarredInfo (final Account.Id forUser)
specifier|public
name|MineStarredInfo
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|forUser
parameter_list|)
block|{
name|owner
operator|=
name|forUser
expr_stmt|;
block|}
DECL|method|getAccounts ()
specifier|public
name|AccountInfoCache
name|getAccounts
parameter_list|()
block|{
return|return
name|accounts
return|;
block|}
DECL|method|setAccounts (final AccountInfoCache ac)
specifier|public
name|void
name|setAccounts
parameter_list|(
specifier|final
name|AccountInfoCache
name|ac
parameter_list|)
block|{
name|accounts
operator|=
name|ac
expr_stmt|;
block|}
DECL|method|getOwner ()
specifier|public
name|Account
operator|.
name|Id
name|getOwner
parameter_list|()
block|{
return|return
name|owner
return|;
block|}
DECL|method|getStarred ()
specifier|public
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|getStarred
parameter_list|()
block|{
return|return
name|starred
return|;
block|}
DECL|method|setStarred (List<ChangeInfo> c)
specifier|public
name|void
name|setStarred
parameter_list|(
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|c
parameter_list|)
block|{
name|starred
operator|=
name|c
expr_stmt|;
block|}
block|}
end_class

end_unit

