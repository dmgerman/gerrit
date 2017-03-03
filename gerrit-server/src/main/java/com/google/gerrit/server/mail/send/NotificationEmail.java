begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.mail.send
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
operator|.
name|send
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
name|Iterables
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
name|errors
operator|.
name|EmailException
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
name|api
operator|.
name|changes
operator|.
name|RecipientType
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
name|Branch
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
name|WatchConfig
operator|.
name|NotifyType
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
name|mail
operator|.
name|Address
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
name|mail
operator|.
name|send
operator|.
name|ProjectWatch
operator|.
name|Watchers
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
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_comment
comment|/** Common class for notifications that are related to a project and branch */
end_comment

begin_class
DECL|class|NotificationEmail
specifier|public
specifier|abstract
class|class
name|NotificationEmail
extends|extends
name|OutgoingEmail
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
name|NotificationEmail
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|branch
specifier|protected
name|Branch
operator|.
name|NameKey
name|branch
decl_stmt|;
DECL|method|NotificationEmail (EmailArguments ea, String mc, Branch.NameKey branch)
specifier|protected
name|NotificationEmail
parameter_list|(
name|EmailArguments
name|ea
parameter_list|,
name|String
name|mc
parameter_list|,
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|)
block|{
name|super
argument_list|(
name|ea
argument_list|,
name|mc
argument_list|)
expr_stmt|;
name|this
operator|.
name|branch
operator|=
name|branch
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init ()
specifier|protected
name|void
name|init
parameter_list|()
throws|throws
name|EmailException
block|{
name|super
operator|.
name|init
argument_list|()
expr_stmt|;
name|setListIdHeader
argument_list|()
expr_stmt|;
block|}
DECL|method|setListIdHeader ()
specifier|private
name|void
name|setListIdHeader
parameter_list|()
throws|throws
name|EmailException
block|{
comment|// Set a reasonable list id so that filters can be used to sort messages
name|setVHeader
argument_list|(
literal|"List-Id"
argument_list|,
literal|"<$email.listId.replace('@', '.')>"
argument_list|)
expr_stmt|;
if|if
condition|(
name|getSettingsUrl
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setVHeader
argument_list|(
literal|"List-Unsubscribe"
argument_list|,
literal|"<$email.settingsUrl>"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getListId ()
specifier|public
name|String
name|getListId
parameter_list|()
throws|throws
name|EmailException
block|{
return|return
name|velocify
argument_list|(
literal|"gerrit-$projectName.replace('/', '-')@$email.gerritHost"
argument_list|)
return|;
block|}
comment|/** Include users and groups that want notification of events. */
DECL|method|includeWatchers (NotifyType type)
specifier|protected
name|void
name|includeWatchers
parameter_list|(
name|NotifyType
name|type
parameter_list|)
block|{
name|includeWatchers
argument_list|(
name|type
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/** Include users and groups that want notification of events. */
DECL|method|includeWatchers (NotifyType type, boolean includeWatchersFromNotifyConfig)
specifier|protected
name|void
name|includeWatchers
parameter_list|(
name|NotifyType
name|type
parameter_list|,
name|boolean
name|includeWatchersFromNotifyConfig
parameter_list|)
block|{
try|try
block|{
name|Watchers
name|matching
init|=
name|getWatchers
argument_list|(
name|type
argument_list|,
name|includeWatchersFromNotifyConfig
argument_list|)
decl_stmt|;
name|add
argument_list|(
name|RecipientType
operator|.
name|TO
argument_list|,
name|matching
operator|.
name|to
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|RecipientType
operator|.
name|CC
argument_list|,
name|matching
operator|.
name|cc
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|RecipientType
operator|.
name|BCC
argument_list|,
name|matching
operator|.
name|bcc
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|err
parameter_list|)
block|{
comment|// Just don't CC everyone. Better to send a partial message to those
comment|// we already have queued up then to fail deliver entirely to people
comment|// who have a lower interest in the change.
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot BCC watchers for "
operator|+
name|type
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Returns all watchers that are relevant */
DECL|method|getWatchers (NotifyType type, boolean includeWatchersFromNotifyConfig)
specifier|protected
specifier|abstract
name|Watchers
name|getWatchers
parameter_list|(
name|NotifyType
name|type
parameter_list|,
name|boolean
name|includeWatchersFromNotifyConfig
parameter_list|)
throws|throws
name|OrmException
function_decl|;
comment|/** Add users or email addresses to the TO, CC, or BCC list. */
DECL|method|add (RecipientType type, Watchers.List list)
specifier|protected
name|void
name|add
parameter_list|(
name|RecipientType
name|type
parameter_list|,
name|Watchers
operator|.
name|List
name|list
parameter_list|)
block|{
for|for
control|(
name|Account
operator|.
name|Id
name|user
range|:
name|list
operator|.
name|accounts
control|)
block|{
name|add
argument_list|(
name|type
argument_list|,
name|user
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Address
name|addr
range|:
name|list
operator|.
name|emails
control|)
block|{
name|add
argument_list|(
name|type
argument_list|,
name|addr
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getSshHost ()
specifier|public
name|String
name|getSshHost
parameter_list|()
block|{
name|String
name|host
init|=
name|Iterables
operator|.
name|getFirst
argument_list|(
name|args
operator|.
name|sshAddresses
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|host
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|host
operator|.
name|startsWith
argument_list|(
literal|"*:"
argument_list|)
condition|)
block|{
return|return
name|getGerritHost
argument_list|()
operator|+
name|host
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
return|;
block|}
return|return
name|host
return|;
block|}
annotation|@
name|Override
DECL|method|setupVelocityContext ()
specifier|protected
name|void
name|setupVelocityContext
parameter_list|()
block|{
name|super
operator|.
name|setupVelocityContext
argument_list|()
expr_stmt|;
name|velocityContext
operator|.
name|put
argument_list|(
literal|"projectName"
argument_list|,
name|branch
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|velocityContext
operator|.
name|put
argument_list|(
literal|"branch"
argument_list|,
name|branch
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setupSoyContext ()
specifier|protected
name|void
name|setupSoyContext
parameter_list|()
block|{
name|super
operator|.
name|setupSoyContext
argument_list|()
expr_stmt|;
name|String
name|projectName
init|=
name|branch
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|soyContext
operator|.
name|put
argument_list|(
literal|"projectName"
argument_list|,
name|projectName
argument_list|)
expr_stmt|;
comment|// shortProjectName is the project name with the path abbreviated.
name|soyContext
operator|.
name|put
argument_list|(
literal|"shortProjectName"
argument_list|,
name|projectName
operator|.
name|replaceAll
argument_list|(
literal|"/.*/"
argument_list|,
literal|"..."
argument_list|)
argument_list|)
expr_stmt|;
name|soyContextEmailData
operator|.
name|put
argument_list|(
literal|"sshHost"
argument_list|,
name|getSshHost
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|branchData
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|branchData
operator|.
name|put
argument_list|(
literal|"shortName"
argument_list|,
name|branch
operator|.
name|getShortName
argument_list|()
argument_list|)
expr_stmt|;
name|soyContext
operator|.
name|put
argument_list|(
literal|"branch"
argument_list|,
name|branchData
argument_list|)
expr_stmt|;
name|footers
operator|.
name|add
argument_list|(
literal|"Gerrit-Project: "
operator|+
name|branch
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|footers
operator|.
name|add
argument_list|(
literal|"Gerrit-Branch: "
operator|+
name|branch
operator|.
name|getShortName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

