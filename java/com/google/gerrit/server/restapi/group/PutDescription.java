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
DECL|package|com.google.gerrit.server.restapi.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
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
name|common
operator|.
name|base
operator|.
name|Strings
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
name|entities
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
name|exceptions
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
name|common
operator|.
name|DescriptionInput
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
name|Response
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
name|RestModifyView
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
name|UserInitiated
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
name|GroupResource
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
name|db
operator|.
name|GroupsUpdate
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
name|db
operator|.
name|InternalGroupUpdate
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|ConfigInvalidException
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|PutDescription
specifier|public
class|class
name|PutDescription
implements|implements
name|RestModifyView
argument_list|<
name|GroupResource
argument_list|,
name|DescriptionInput
argument_list|>
block|{
DECL|field|groupsUpdateProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|GroupsUpdate
argument_list|>
name|groupsUpdateProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|PutDescription (@serInitiated Provider<GroupsUpdate> groupsUpdateProvider)
name|PutDescription
parameter_list|(
annotation|@
name|UserInitiated
name|Provider
argument_list|<
name|GroupsUpdate
argument_list|>
name|groupsUpdateProvider
parameter_list|)
block|{
name|this
operator|.
name|groupsUpdateProvider
operator|=
name|groupsUpdateProvider
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (GroupResource resource, DescriptionInput input)
specifier|public
name|Response
argument_list|<
name|String
argument_list|>
name|apply
parameter_list|(
name|GroupResource
name|resource
parameter_list|,
name|DescriptionInput
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|NotInternalGroupException
throws|,
name|ResourceNotFoundException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
name|input
operator|=
operator|new
name|DescriptionInput
argument_list|()
expr_stmt|;
comment|// Delete would set description to null.
block|}
name|GroupDescription
operator|.
name|Internal
name|internalGroup
init|=
name|resource
operator|.
name|asInternalGroup
argument_list|()
operator|.
name|orElseThrow
argument_list|(
name|NotInternalGroupException
operator|::
operator|new
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|resource
operator|.
name|getControl
argument_list|()
operator|.
name|isOwner
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"Not group owner"
argument_list|)
throw|;
block|}
name|String
name|currentDescription
init|=
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|internalGroup
operator|.
name|getDescription
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|newDescription
init|=
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|input
operator|.
name|description
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Objects
operator|.
name|equals
argument_list|(
name|currentDescription
argument_list|,
name|newDescription
argument_list|)
condition|)
block|{
name|AccountGroup
operator|.
name|UUID
name|groupUuid
init|=
name|internalGroup
operator|.
name|getGroupUUID
argument_list|()
decl_stmt|;
name|InternalGroupUpdate
name|groupUpdate
init|=
name|InternalGroupUpdate
operator|.
name|builder
argument_list|()
operator|.
name|setDescription
argument_list|(
name|newDescription
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
try|try
block|{
name|groupsUpdateProvider
operator|.
name|get
argument_list|()
operator|.
name|updateGroup
argument_list|(
name|groupUuid
argument_list|,
name|groupUpdate
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchGroupException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Group %s not found"
argument_list|,
name|groupUuid
argument_list|)
argument_list|)
throw|;
block|}
block|}
return|return
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|input
operator|.
name|description
argument_list|)
condition|?
name|Response
operator|.
name|none
argument_list|()
else|:
name|Response
operator|.
name|ok
argument_list|(
name|input
operator|.
name|description
argument_list|)
return|;
block|}
block|}
end_class

end_unit

