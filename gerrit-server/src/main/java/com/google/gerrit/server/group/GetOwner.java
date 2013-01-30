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
name|account
operator|.
name|GroupControl
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

begin_class
DECL|class|GetOwner
specifier|public
class|class
name|GetOwner
implements|implements
name|RestReadView
argument_list|<
name|GroupResource
argument_list|>
block|{
DECL|field|controlFactory
specifier|private
specifier|final
name|GroupControl
operator|.
name|Factory
name|controlFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetOwner (GroupControl.Factory controlFactory)
name|GetOwner
parameter_list|(
name|GroupControl
operator|.
name|Factory
name|controlFactory
parameter_list|)
block|{
name|this
operator|.
name|controlFactory
operator|=
name|controlFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (GroupResource resource)
specifier|public
name|Object
name|apply
parameter_list|(
name|GroupResource
name|resource
parameter_list|)
throws|throws
name|ResourceNotFoundException
block|{
name|AccountGroup
name|group
init|=
name|resource
operator|.
name|toAccountGroup
argument_list|()
decl_stmt|;
if|if
condition|(
name|group
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|()
throw|;
block|}
try|try
block|{
name|GroupControl
name|c
init|=
name|controlFactory
operator|.
name|validateFor
argument_list|(
name|group
operator|.
name|getOwnerGroupUUID
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|GroupInfo
argument_list|(
name|c
operator|.
name|getGroup
argument_list|()
argument_list|)
return|;
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
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

