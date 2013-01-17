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
name|common
operator|.
name|collect
operator|.
name|Lists
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
name|NoSuchGroupException
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
name|RestReadView
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
name|group
operator|.
name|GroupInfo
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|GetGroups
class|class
name|GetGroups
implements|implements
name|RestReadView
argument_list|<
name|AccountResource
argument_list|>
block|{
DECL|field|groupControlFactory
specifier|private
specifier|final
name|GroupControl
operator|.
name|Factory
name|groupControlFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetGroups (GroupControl.Factory groupControlFactory)
name|GetGroups
parameter_list|(
name|GroupControl
operator|.
name|Factory
name|groupControlFactory
parameter_list|)
block|{
name|this
operator|.
name|groupControlFactory
operator|=
name|groupControlFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (AccountResource resource)
specifier|public
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|apply
parameter_list|(
name|AccountResource
name|resource
parameter_list|)
block|{
name|IdentifiedUser
name|user
init|=
name|resource
operator|.
name|getUser
argument_list|()
decl_stmt|;
name|Account
operator|.
name|Id
name|userId
init|=
name|user
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|groups
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|uuid
range|:
name|user
operator|.
name|getEffectiveGroups
argument_list|()
operator|.
name|getKnownGroups
argument_list|()
control|)
block|{
name|GroupControl
name|ctl
decl_stmt|;
try|try
block|{
name|ctl
operator|=
name|groupControlFactory
operator|.
name|controlFor
argument_list|(
name|uuid
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchGroupException
name|e
parameter_list|)
block|{
continue|continue;
block|}
if|if
condition|(
name|ctl
operator|.
name|isVisible
argument_list|()
operator|&&
name|ctl
operator|.
name|canSeeMember
argument_list|(
name|userId
argument_list|)
condition|)
block|{
name|groups
operator|.
name|add
argument_list|(
operator|new
name|GroupInfo
argument_list|(
name|ctl
operator|.
name|getGroup
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|groups
return|;
block|}
block|}
end_class

end_unit

