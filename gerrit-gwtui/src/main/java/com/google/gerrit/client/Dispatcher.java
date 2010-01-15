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
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|PageLinks
operator|.
name|ADMIN_GROUPS
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|PageLinks
operator|.
name|ADMIN_PROJECTS
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|PageLinks
operator|.
name|MINE
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|PageLinks
operator|.
name|MINE_DRAFTS
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|PageLinks
operator|.
name|MINE_STARRED
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|PageLinks
operator|.
name|REGISTER
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|PageLinks
operator|.
name|SETTINGS
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|PageLinks
operator|.
name|SETTINGS_NEW_AGREEMENT
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|PageLinks
operator|.
name|SETTINGS_WEBIDENT
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|PageLinks
operator|.
name|TOP
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
name|account
operator|.
name|NewAgreementScreen
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
name|account
operator|.
name|RegisterScreen
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
name|account
operator|.
name|ValidateEmailScreen
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
name|admin
operator|.
name|AccountGroupScreen
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
name|admin
operator|.
name|GroupListScreen
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
name|admin
operator|.
name|ProjectAdminScreen
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
name|admin
operator|.
name|ProjectListScreen
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
name|auth
operator|.
name|openid
operator|.
name|OpenIdSignInDialog
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
name|auth
operator|.
name|userpass
operator|.
name|UserPassSignInDialog
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
name|AllAbandonedChangesScreen
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
name|AllMergedChangesScreen
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
name|AllOpenChangesScreen
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
name|ByProjectAbandonedChangesScreen
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
name|ByProjectMergedChangesScreen
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
name|ByProjectOpenChangesScreen
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
name|ChangeQueryResultsScreen
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
name|MineDraftsScreen
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
name|changes
operator|.
name|PatchTable
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
name|PublishCommentScreen
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
name|patches
operator|.
name|PatchScreen
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
name|gerrit
operator|.
name|common
operator|.
name|PageLinks
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
name|auth
operator|.
name|SignInMode
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
name|Change
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
name|Patch
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
name|PatchSet
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
name|Project
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
name|GWT
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
name|RunAsyncCallback
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
name|client
operator|.
name|KeyUtil
import|;
end_import

begin_class
DECL|class|Dispatcher
specifier|public
class|class
name|Dispatcher
block|{
DECL|method|toPatchSideBySide (final Patch.Key id)
specifier|public
specifier|static
name|String
name|toPatchSideBySide
parameter_list|(
specifier|final
name|Patch
operator|.
name|Key
name|id
parameter_list|)
block|{
return|return
name|toPatch
argument_list|(
literal|"sidebyside"
argument_list|,
name|id
argument_list|)
return|;
block|}
DECL|method|toPatchUnified (final Patch.Key id)
specifier|public
specifier|static
name|String
name|toPatchUnified
parameter_list|(
specifier|final
name|Patch
operator|.
name|Key
name|id
parameter_list|)
block|{
return|return
name|toPatch
argument_list|(
literal|"unified"
argument_list|,
name|id
argument_list|)
return|;
block|}
DECL|method|toPatch (final String type, final Patch.Key id)
specifier|public
specifier|static
name|String
name|toPatch
parameter_list|(
specifier|final
name|String
name|type
parameter_list|,
specifier|final
name|Patch
operator|.
name|Key
name|id
parameter_list|)
block|{
return|return
literal|"patch,"
operator|+
name|type
operator|+
literal|","
operator|+
name|id
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|toAccountGroup (final AccountGroup.Id id)
specifier|public
specifier|static
name|String
name|toAccountGroup
parameter_list|(
specifier|final
name|AccountGroup
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
literal|"admin,group,"
operator|+
name|id
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|toProjectAdmin (final Project.NameKey n, final String tab)
specifier|public
specifier|static
name|String
name|toProjectAdmin
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|n
parameter_list|,
specifier|final
name|String
name|tab
parameter_list|)
block|{
return|return
literal|"admin,project,"
operator|+
name|n
operator|.
name|toString
argument_list|()
operator|+
literal|","
operator|+
name|tab
return|;
block|}
DECL|method|display (final String token)
name|void
name|display
parameter_list|(
specifier|final
name|String
name|token
parameter_list|)
block|{
assert|assert
name|token
operator|!=
literal|null
assert|;
try|try
block|{
name|select
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|err
parameter_list|)
block|{
name|GWT
operator|.
name|log
argument_list|(
literal|"Error parsing history token: "
operator|+
name|token
argument_list|,
name|err
argument_list|)
expr_stmt|;
name|Gerrit
operator|.
name|display
argument_list|(
name|token
argument_list|,
operator|new
name|NotFoundScreen
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|select (final String token)
specifier|private
specifier|static
name|void
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
operator|.
name|startsWith
argument_list|(
literal|"patch,"
argument_list|)
condition|)
block|{
name|patch
argument_list|(
name|token
argument_list|,
literal|null
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
literal|"change,publish,"
argument_list|)
condition|)
block|{
name|publish
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|MINE
operator|.
name|equals
argument_list|(
name|token
argument_list|)
operator|||
name|token
operator|.
name|startsWith
argument_list|(
literal|"mine,"
argument_list|)
condition|)
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|token
argument_list|,
name|mine
argument_list|(
name|token
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
literal|"all,"
argument_list|)
condition|)
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|token
argument_list|,
name|all
argument_list|(
name|token
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
literal|"project,"
argument_list|)
condition|)
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|token
argument_list|,
name|project
argument_list|(
name|token
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SETTINGS
operator|.
name|equals
argument_list|(
name|token
argument_list|)
comment|//
operator|||
name|REGISTER
operator|.
name|equals
argument_list|(
name|token
argument_list|)
comment|//
operator|||
name|token
operator|.
name|startsWith
argument_list|(
literal|"settings,"
argument_list|)
comment|//
operator|||
name|token
operator|.
name|startsWith
argument_list|(
literal|"register,"
argument_list|)
comment|//
operator|||
name|token
operator|.
name|startsWith
argument_list|(
literal|"VE,"
argument_list|)
comment|//
operator|||
name|token
operator|.
name|startsWith
argument_list|(
literal|"SignInFailure,"
argument_list|)
condition|)
block|{
name|settings
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
literal|"admin,"
argument_list|)
condition|)
block|{
name|admin
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|token
argument_list|,
name|core
argument_list|(
name|token
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|mine (final String token)
specifier|private
specifier|static
name|Screen
name|mine
parameter_list|(
specifier|final
name|String
name|token
parameter_list|)
block|{
if|if
condition|(
name|MINE
operator|.
name|equals
argument_list|(
name|token
argument_list|)
condition|)
block|{
if|if
condition|(
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
condition|)
block|{
return|return
operator|new
name|AccountDashboardScreen
argument_list|(
name|Gerrit
operator|.
name|getUserAccount
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
specifier|final
name|Screen
name|r
init|=
operator|new
name|AccountDashboardScreen
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|r
operator|.
name|setRequiresSignIn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
block|}
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
block|{
return|return
operator|new
name|MineStarredScreen
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|MINE_DRAFTS
operator|.
name|equals
argument_list|(
name|token
argument_list|)
condition|)
block|{
return|return
operator|new
name|MineDraftsScreen
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|new
name|NotFoundScreen
argument_list|()
return|;
block|}
block|}
DECL|method|all (final String token)
specifier|private
specifier|static
name|Screen
name|all
parameter_list|(
specifier|final
name|String
name|token
parameter_list|)
block|{
name|String
name|p
decl_stmt|;
name|p
operator|=
literal|"all,abandoned,"
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
condition|)
block|{
return|return
operator|new
name|AllAbandonedChangesScreen
argument_list|(
name|skip
argument_list|(
name|p
argument_list|,
name|token
argument_list|)
argument_list|)
return|;
block|}
name|p
operator|=
literal|"all,merged,"
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
condition|)
block|{
return|return
operator|new
name|AllMergedChangesScreen
argument_list|(
name|skip
argument_list|(
name|p
argument_list|,
name|token
argument_list|)
argument_list|)
return|;
block|}
name|p
operator|=
literal|"all,open,"
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
condition|)
block|{
return|return
operator|new
name|AllOpenChangesScreen
argument_list|(
name|skip
argument_list|(
name|p
argument_list|,
name|token
argument_list|)
argument_list|)
return|;
block|}
return|return
operator|new
name|NotFoundScreen
argument_list|()
return|;
block|}
DECL|method|project (final String token)
specifier|private
specifier|static
name|Screen
name|project
parameter_list|(
specifier|final
name|String
name|token
parameter_list|)
block|{
name|String
name|p
decl_stmt|;
name|p
operator|=
literal|"project,open,"
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
condition|)
block|{
specifier|final
name|String
name|s
init|=
name|skip
argument_list|(
name|p
argument_list|,
name|token
argument_list|)
decl_stmt|;
specifier|final
name|int
name|c
init|=
name|s
operator|.
name|indexOf
argument_list|(
literal|','
argument_list|)
decl_stmt|;
return|return
operator|new
name|ByProjectOpenChangesScreen
argument_list|(
name|Project
operator|.
name|NameKey
operator|.
name|parse
argument_list|(
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|c
argument_list|)
argument_list|)
argument_list|,
name|s
operator|.
name|substring
argument_list|(
name|c
operator|+
literal|1
argument_list|)
argument_list|)
return|;
block|}
name|p
operator|=
literal|"project,merged,"
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
condition|)
block|{
specifier|final
name|String
name|s
init|=
name|skip
argument_list|(
name|p
argument_list|,
name|token
argument_list|)
decl_stmt|;
specifier|final
name|int
name|c
init|=
name|s
operator|.
name|indexOf
argument_list|(
literal|','
argument_list|)
decl_stmt|;
return|return
operator|new
name|ByProjectMergedChangesScreen
argument_list|(
name|Project
operator|.
name|NameKey
operator|.
name|parse
argument_list|(
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|c
argument_list|)
argument_list|)
argument_list|,
name|s
operator|.
name|substring
argument_list|(
name|c
operator|+
literal|1
argument_list|)
argument_list|)
return|;
block|}
name|p
operator|=
literal|"project,abandoned,"
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
condition|)
block|{
specifier|final
name|String
name|s
init|=
name|skip
argument_list|(
name|p
argument_list|,
name|token
argument_list|)
decl_stmt|;
specifier|final
name|int
name|c
init|=
name|s
operator|.
name|indexOf
argument_list|(
literal|','
argument_list|)
decl_stmt|;
return|return
operator|new
name|ByProjectAbandonedChangesScreen
argument_list|(
name|Project
operator|.
name|NameKey
operator|.
name|parse
argument_list|(
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|c
argument_list|)
argument_list|)
argument_list|,
name|s
operator|.
name|substring
argument_list|(
name|c
operator|+
literal|1
argument_list|)
argument_list|)
return|;
block|}
return|return
operator|new
name|NotFoundScreen
argument_list|()
return|;
block|}
DECL|method|core (final String token)
specifier|private
specifier|static
name|Screen
name|core
parameter_list|(
specifier|final
name|String
name|token
parameter_list|)
block|{
name|String
name|p
decl_stmt|;
name|p
operator|=
literal|"change,"
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
condition|)
return|return
operator|new
name|ChangeScreen
argument_list|(
name|Change
operator|.
name|Id
operator|.
name|parse
argument_list|(
name|skip
argument_list|(
name|p
argument_list|,
name|token
argument_list|)
argument_list|)
argument_list|)
return|;
name|p
operator|=
literal|"dashboard,"
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
condition|)
return|return
operator|new
name|AccountDashboardScreen
argument_list|(
name|Account
operator|.
name|Id
operator|.
name|parse
argument_list|(
name|skip
argument_list|(
name|p
argument_list|,
name|token
argument_list|)
argument_list|)
argument_list|)
return|;
name|p
operator|=
literal|"q,"
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
condition|)
block|{
specifier|final
name|String
name|s
init|=
name|skip
argument_list|(
name|p
argument_list|,
name|token
argument_list|)
decl_stmt|;
specifier|final
name|int
name|c
init|=
name|s
operator|.
name|indexOf
argument_list|(
literal|','
argument_list|)
decl_stmt|;
return|return
operator|new
name|ChangeQueryResultsScreen
argument_list|(
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|c
argument_list|)
argument_list|,
name|s
operator|.
name|substring
argument_list|(
name|c
operator|+
literal|1
argument_list|)
argument_list|)
return|;
block|}
return|return
operator|new
name|NotFoundScreen
argument_list|()
return|;
block|}
DECL|method|publish (final String token)
specifier|private
specifier|static
name|void
name|publish
parameter_list|(
specifier|final
name|String
name|token
parameter_list|)
block|{
operator|new
name|RunAsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|()
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|token
argument_list|,
name|select
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Screen
name|select
parameter_list|()
block|{
name|String
name|p
init|=
literal|"change,publish,"
decl_stmt|;
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
condition|)
return|return
operator|new
name|PublishCommentScreen
argument_list|(
name|PatchSet
operator|.
name|Id
operator|.
name|parse
argument_list|(
name|skip
argument_list|(
name|p
argument_list|,
name|token
argument_list|)
argument_list|)
argument_list|)
return|;
return|return
operator|new
name|NotFoundScreen
argument_list|()
return|;
block|}
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|reason
parameter_list|)
block|{
operator|new
name|ErrorDialog
argument_list|(
name|reason
argument_list|)
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
block|}
operator|.
name|onSuccess
argument_list|()
expr_stmt|;
block|}
DECL|method|patch (final String token, final Patch.Key id, final int patchIndex, final PatchTable patchTable)
specifier|public
specifier|static
name|void
name|patch
parameter_list|(
specifier|final
name|String
name|token
parameter_list|,
specifier|final
name|Patch
operator|.
name|Key
name|id
parameter_list|,
specifier|final
name|int
name|patchIndex
parameter_list|,
specifier|final
name|PatchTable
name|patchTable
parameter_list|)
block|{
name|GWT
operator|.
name|runAsync
argument_list|(
operator|new
name|RunAsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|()
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|token
argument_list|,
name|select
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Screen
name|select
parameter_list|()
block|{
name|String
name|p
decl_stmt|;
name|p
operator|=
literal|"patch,sidebyside,"
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
condition|)
block|{
return|return
operator|new
name|PatchScreen
operator|.
name|SideBySide
argument_list|(
comment|//
name|id
operator|!=
literal|null
condition|?
name|id
else|:
name|Patch
operator|.
name|Key
operator|.
name|parse
argument_list|(
name|skip
argument_list|(
name|p
argument_list|,
name|token
argument_list|)
argument_list|)
argument_list|,
comment|//
name|patchIndex
argument_list|,
comment|//
name|patchTable
comment|//
argument_list|)
return|;
block|}
name|p
operator|=
literal|"patch,unified,"
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
condition|)
block|{
return|return
operator|new
name|PatchScreen
operator|.
name|Unified
argument_list|(
comment|//
name|id
operator|!=
literal|null
condition|?
name|id
else|:
name|Patch
operator|.
name|Key
operator|.
name|parse
argument_list|(
name|skip
argument_list|(
name|p
argument_list|,
name|token
argument_list|)
argument_list|)
argument_list|,
comment|//
name|patchIndex
argument_list|,
comment|//
name|patchTable
comment|//
argument_list|)
return|;
block|}
return|return
operator|new
name|NotFoundScreen
argument_list|()
return|;
block|}
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|reason
parameter_list|)
block|{
operator|new
name|ErrorDialog
argument_list|(
name|reason
argument_list|)
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|settings (final String token)
specifier|private
specifier|static
name|void
name|settings
parameter_list|(
specifier|final
name|String
name|token
parameter_list|)
block|{
name|GWT
operator|.
name|runAsync
argument_list|(
operator|new
name|RunAsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|()
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|token
argument_list|,
name|select
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Screen
name|select
parameter_list|()
block|{
name|String
name|p
decl_stmt|;
name|p
operator|=
literal|"register,"
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
condition|)
block|{
return|return
operator|new
name|RegisterScreen
argument_list|(
name|skip
argument_list|(
name|p
argument_list|,
name|token
argument_list|)
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|REGISTER
operator|.
name|equals
argument_list|(
name|token
argument_list|)
condition|)
block|{
return|return
operator|new
name|RegisterScreen
argument_list|(
name|MINE
argument_list|)
return|;
block|}
name|p
operator|=
literal|"VE,"
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
condition|)
return|return
operator|new
name|ValidateEmailScreen
argument_list|(
name|skip
argument_list|(
name|p
argument_list|,
name|token
argument_list|)
argument_list|)
return|;
name|p
operator|=
literal|"SignInFailure,"
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
condition|)
block|{
specifier|final
name|String
index|[]
name|args
init|=
name|skip
argument_list|(
name|p
argument_list|,
name|token
argument_list|)
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
specifier|final
name|SignInMode
name|mode
init|=
name|SignInMode
operator|.
name|valueOf
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
specifier|final
name|String
name|msg
init|=
name|KeyUtil
operator|.
name|decode
argument_list|(
name|args
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
specifier|final
name|String
name|to
init|=
name|PageLinks
operator|.
name|MINE
decl_stmt|;
switch|switch
condition|(
name|Gerrit
operator|.
name|getConfig
argument_list|()
operator|.
name|getAuthType
argument_list|()
condition|)
block|{
case|case
name|OPENID
case|:
operator|new
name|OpenIdSignInDialog
argument_list|(
name|mode
argument_list|,
name|to
argument_list|,
name|msg
argument_list|)
operator|.
name|center
argument_list|()
expr_stmt|;
break|break;
case|case
name|LDAP
case|:
operator|new
name|UserPassSignInDialog
argument_list|(
name|to
argument_list|,
name|msg
argument_list|)
operator|.
name|center
argument_list|()
expr_stmt|;
break|break;
default|default:
return|return
literal|null
return|;
block|}
switch|switch
condition|(
name|mode
condition|)
block|{
case|case
name|SIGN_IN
case|:
return|return
operator|new
name|AllOpenChangesScreen
argument_list|(
name|TOP
argument_list|)
return|;
case|case
name|LINK_IDENTIY
case|:
return|return
operator|new
name|AccountSettings
argument_list|(
name|SETTINGS_WEBIDENT
argument_list|)
return|;
block|}
block|}
if|if
condition|(
name|SETTINGS_NEW_AGREEMENT
operator|.
name|equals
argument_list|(
name|token
argument_list|)
condition|)
return|return
operator|new
name|NewAgreementScreen
argument_list|()
return|;
name|p
operator|=
name|SETTINGS_NEW_AGREEMENT
operator|+
literal|","
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
condition|)
block|{
return|return
operator|new
name|NewAgreementScreen
argument_list|(
name|skip
argument_list|(
name|p
argument_list|,
name|token
argument_list|)
argument_list|)
return|;
block|}
return|return
operator|new
name|AccountSettings
argument_list|(
name|token
argument_list|)
return|;
block|}
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|reason
parameter_list|)
block|{
operator|new
name|ErrorDialog
argument_list|(
name|reason
argument_list|)
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|admin (final String token)
specifier|private
specifier|static
name|void
name|admin
parameter_list|(
specifier|final
name|String
name|token
parameter_list|)
block|{
name|GWT
operator|.
name|runAsync
argument_list|(
operator|new
name|RunAsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|()
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|token
argument_list|,
name|select
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Screen
name|select
parameter_list|()
block|{
name|String
name|p
decl_stmt|;
name|p
operator|=
literal|"admin,group,"
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
condition|)
return|return
operator|new
name|AccountGroupScreen
argument_list|(
name|AccountGroup
operator|.
name|Id
operator|.
name|parse
argument_list|(
name|skip
argument_list|(
name|p
argument_list|,
name|token
argument_list|)
argument_list|)
argument_list|)
return|;
name|p
operator|=
literal|"admin,project,"
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|p
argument_list|)
condition|)
block|{
name|p
operator|=
name|skip
argument_list|(
name|p
argument_list|,
name|token
argument_list|)
expr_stmt|;
specifier|final
name|int
name|c
init|=
name|p
operator|.
name|indexOf
argument_list|(
literal|','
argument_list|)
decl_stmt|;
specifier|final
name|String
name|idstr
init|=
name|p
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|c
argument_list|)
decl_stmt|;
return|return
operator|new
name|ProjectAdminScreen
argument_list|(
name|Project
operator|.
name|NameKey
operator|.
name|parse
argument_list|(
name|idstr
argument_list|)
argument_list|,
name|token
argument_list|)
return|;
block|}
if|if
condition|(
name|ADMIN_GROUPS
operator|.
name|equals
argument_list|(
name|token
argument_list|)
condition|)
block|{
return|return
operator|new
name|GroupListScreen
argument_list|()
return|;
block|}
if|if
condition|(
name|ADMIN_PROJECTS
operator|.
name|equals
argument_list|(
name|token
argument_list|)
condition|)
block|{
return|return
operator|new
name|ProjectListScreen
argument_list|()
return|;
block|}
return|return
operator|new
name|NotFoundScreen
argument_list|()
return|;
block|}
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|reason
parameter_list|)
block|{
operator|new
name|ErrorDialog
argument_list|(
name|reason
argument_list|)
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|skip (final String prefix, final String in)
specifier|private
specifier|static
name|String
name|skip
parameter_list|(
specifier|final
name|String
name|prefix
parameter_list|,
specifier|final
name|String
name|in
parameter_list|)
block|{
return|return
name|in
operator|.
name|substring
argument_list|(
name|prefix
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

