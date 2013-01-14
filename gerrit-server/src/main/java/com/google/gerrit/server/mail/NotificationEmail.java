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
DECL|package|com.google.gerrit.server.mail
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
name|reviewdb
operator|.
name|client
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|AccountProjectWatch
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
comment|/**  * Common class for notifications that are related to a project and branch  */
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
DECL|field|project
specifier|protected
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|field|branch
specifier|protected
name|Branch
operator|.
name|NameKey
name|branch
decl_stmt|;
DECL|method|NotificationEmail (EmailArguments ea, String anonymousCowardName, String mc, Project.NameKey project, Branch.NameKey branch)
specifier|protected
name|NotificationEmail
parameter_list|(
name|EmailArguments
name|ea
parameter_list|,
name|String
name|anonymousCowardName
parameter_list|,
name|String
name|mc
parameter_list|,
name|Project
operator|.
name|NameKey
name|project
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
name|anonymousCowardName
argument_list|,
name|mc
argument_list|)
expr_stmt|;
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|this
operator|.
name|branch
operator|=
name|branch
expr_stmt|;
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
try|try
block|{
name|Watchers
name|matching
init|=
name|getWatchers
argument_list|(
name|type
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
DECL|method|getWatchers (NotifyType type)
specifier|protected
specifier|abstract
name|Watchers
name|getWatchers
parameter_list|(
name|NotifyType
name|type
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
name|project
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
block|}
end_class

end_unit

