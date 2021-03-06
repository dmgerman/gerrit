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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|entities
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
name|extensions
operator|.
name|restapi
operator|.
name|RestResource
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
name|change
operator|.
name|ChangeResource
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
name|TypeLiteral
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

begin_class
DECL|class|AccountResource
specifier|public
class|class
name|AccountResource
implements|implements
name|RestResource
block|{
DECL|field|ACCOUNT_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|AccountResource
argument_list|>
argument_list|>
name|ACCOUNT_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|AccountResource
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|CAPABILITY_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|Capability
argument_list|>
argument_list|>
name|CAPABILITY_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|Capability
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|EMAIL_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|Email
argument_list|>
argument_list|>
name|EMAIL_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|Email
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|SSH_KEY_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|SshKey
argument_list|>
argument_list|>
name|SSH_KEY_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|SshKey
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|STARRED_CHANGE_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|StarredChange
argument_list|>
argument_list|>
name|STARRED_CHANGE_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|StarredChange
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
DECL|method|AccountResource (IdentifiedUser user)
specifier|public
name|AccountResource
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|)
block|{
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
DECL|method|getUser ()
specifier|public
name|IdentifiedUser
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
DECL|class|Capability
specifier|public
specifier|static
class|class
name|Capability
implements|implements
name|RestResource
block|{
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
DECL|field|capability
specifier|private
specifier|final
name|String
name|capability
decl_stmt|;
DECL|method|Capability (IdentifiedUser user, String capability)
specifier|public
name|Capability
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|,
name|String
name|capability
parameter_list|)
block|{
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|capability
operator|=
name|capability
expr_stmt|;
block|}
DECL|method|getUser ()
specifier|public
name|IdentifiedUser
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
DECL|method|getCapability ()
specifier|public
name|String
name|getCapability
parameter_list|()
block|{
return|return
name|capability
return|;
block|}
block|}
DECL|class|Email
specifier|public
specifier|static
class|class
name|Email
extends|extends
name|AccountResource
block|{
DECL|field|email
specifier|private
specifier|final
name|String
name|email
decl_stmt|;
DECL|method|Email (IdentifiedUser user, String email)
specifier|public
name|Email
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|,
name|String
name|email
parameter_list|)
block|{
name|super
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|this
operator|.
name|email
operator|=
name|email
expr_stmt|;
block|}
DECL|method|getEmail ()
specifier|public
name|String
name|getEmail
parameter_list|()
block|{
return|return
name|email
return|;
block|}
block|}
DECL|class|SshKey
specifier|public
specifier|static
class|class
name|SshKey
extends|extends
name|AccountResource
block|{
DECL|field|sshKey
specifier|private
specifier|final
name|AccountSshKey
name|sshKey
decl_stmt|;
DECL|method|SshKey (IdentifiedUser user, AccountSshKey sshKey)
specifier|public
name|SshKey
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|,
name|AccountSshKey
name|sshKey
parameter_list|)
block|{
name|super
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|this
operator|.
name|sshKey
operator|=
name|sshKey
expr_stmt|;
block|}
DECL|method|getSshKey ()
specifier|public
name|AccountSshKey
name|getSshKey
parameter_list|()
block|{
return|return
name|sshKey
return|;
block|}
block|}
DECL|class|StarredChange
specifier|public
specifier|static
class|class
name|StarredChange
extends|extends
name|AccountResource
block|{
DECL|field|change
specifier|private
specifier|final
name|ChangeResource
name|change
decl_stmt|;
DECL|method|StarredChange (IdentifiedUser user, ChangeResource change)
specifier|public
name|StarredChange
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|,
name|ChangeResource
name|change
parameter_list|)
block|{
name|super
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|this
operator|.
name|change
operator|=
name|change
expr_stmt|;
block|}
DECL|method|getChange ()
specifier|public
name|Change
name|getChange
parameter_list|()
block|{
return|return
name|change
operator|.
name|getChange
argument_list|()
return|;
block|}
block|}
DECL|class|Star
specifier|public
specifier|static
class|class
name|Star
implements|implements
name|RestResource
block|{
DECL|field|STAR_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|Star
argument_list|>
argument_list|>
name|STAR_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|Star
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
DECL|field|change
specifier|private
specifier|final
name|ChangeResource
name|change
decl_stmt|;
DECL|field|labels
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|labels
decl_stmt|;
DECL|method|Star (IdentifiedUser user, ChangeResource change, Set<String> labels)
specifier|public
name|Star
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|,
name|ChangeResource
name|change
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|labels
parameter_list|)
block|{
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|change
operator|=
name|change
expr_stmt|;
name|this
operator|.
name|labels
operator|=
name|labels
expr_stmt|;
block|}
DECL|method|getUser ()
specifier|public
name|IdentifiedUser
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
DECL|method|getChange ()
specifier|public
name|Change
name|getChange
parameter_list|()
block|{
return|return
name|change
operator|.
name|getChange
argument_list|()
return|;
block|}
DECL|method|getLabels ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getLabels
parameter_list|()
block|{
return|return
name|labels
return|;
block|}
block|}
block|}
end_class

end_unit

