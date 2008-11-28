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
DECL|package|com.google.gerrit.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|account
operator|.
name|AccountSettings
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
name|changes
operator|.
name|ChangeScreen
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
name|changes
operator|.
name|AccountDashboardScreen
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
name|data
operator|.
name|AccountInfo
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
name|ChangeInfo
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|ui
operator|.
name|Screen
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
name|HistoryListener
import|;
end_import

begin_class
DECL|class|Link
specifier|public
class|class
name|Link
implements|implements
name|HistoryListener
block|{
DECL|field|SETTINGS
specifier|public
specifier|static
specifier|final
name|String
name|SETTINGS
init|=
literal|"settings"
decl_stmt|;
DECL|field|MINE
specifier|public
specifier|static
specifier|final
name|String
name|MINE
init|=
literal|"mine"
decl_stmt|;
DECL|field|MINE_UNCLAIMED
specifier|public
specifier|static
specifier|final
name|String
name|MINE_UNCLAIMED
init|=
literal|"mine,unclaimed"
decl_stmt|;
DECL|field|MINE_STARRED
specifier|public
specifier|static
specifier|final
name|String
name|MINE_STARRED
init|=
literal|"mine,starred"
decl_stmt|;
DECL|field|ALL
specifier|public
specifier|static
specifier|final
name|String
name|ALL
init|=
literal|"all"
decl_stmt|;
DECL|field|ALL_UNCLAIMED
specifier|public
specifier|static
specifier|final
name|String
name|ALL_UNCLAIMED
init|=
literal|"all,unclaimed"
decl_stmt|;
DECL|field|ADMIN_PEOPLE
specifier|public
specifier|static
specifier|final
name|String
name|ADMIN_PEOPLE
init|=
literal|"admin,people"
decl_stmt|;
DECL|field|ADMIN_GROUPS
specifier|public
specifier|static
specifier|final
name|String
name|ADMIN_GROUPS
init|=
literal|"admin,groups"
decl_stmt|;
DECL|field|ADMIN_PROJECTS
specifier|public
specifier|static
specifier|final
name|String
name|ADMIN_PROJECTS
init|=
literal|"admin,projects"
decl_stmt|;
DECL|method|toChange (final ChangeInfo c)
specifier|public
specifier|static
name|String
name|toChange
parameter_list|(
specifier|final
name|ChangeInfo
name|c
parameter_list|)
block|{
return|return
literal|"change,"
operator|+
name|c
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|toAccountDashboard (final AccountInfo acct)
specifier|public
specifier|static
name|String
name|toAccountDashboard
parameter_list|(
specifier|final
name|AccountInfo
name|acct
parameter_list|)
block|{
return|return
literal|"dashboard,"
operator|+
name|acct
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|onHistoryChanged (final String token)
specifier|public
name|void
name|onHistoryChanged
parameter_list|(
specifier|final
name|String
name|token
parameter_list|)
block|{
specifier|final
name|Screen
name|s
init|=
name|select
argument_list|(
name|token
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Gerrit
operator|.
name|display
argument_list|(
operator|new
name|NotFoundScreen
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|select (final String token)
specifier|private
name|Screen
name|select
parameter_list|(
specifier|final
name|String
name|token
parameter_list|)
block|{
if|if
condition|(
name|token
operator|==
literal|null
condition|)
return|return
literal|null
return|;
elseif|else
if|if
condition|(
name|SETTINGS
operator|.
name|equals
argument_list|(
name|token
argument_list|)
condition|)
return|return
operator|new
name|AccountSettings
argument_list|()
return|;
elseif|else
if|if
condition|(
name|MINE
operator|.
name|equals
argument_list|(
name|token
argument_list|)
condition|)
return|return
operator|new
name|AccountDashboardScreen
argument_list|()
return|;
elseif|else
if|if
condition|(
name|MINE_STARRED
operator|.
name|equals
argument_list|(
name|token
argument_list|)
condition|)
return|return
operator|new
name|MineStarredScreen
argument_list|()
return|;
elseif|else
if|if
condition|(
name|token
operator|.
name|matches
argument_list|(
literal|"^change,\\d+$"
argument_list|)
condition|)
block|{
specifier|final
name|String
name|id
init|=
name|token
operator|.
name|substring
argument_list|(
literal|"change,"
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|ChangeScreen
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|token
operator|.
name|matches
argument_list|(
literal|"^dashboard,\\d+$"
argument_list|)
condition|)
block|{
specifier|final
name|String
name|id
init|=
name|token
operator|.
name|substring
argument_list|(
literal|"dashboard,"
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|AccountDashboardScreen
argument_list|(
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|id
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

