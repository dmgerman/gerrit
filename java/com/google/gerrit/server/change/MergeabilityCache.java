begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|client
operator|.
name|SubmitType
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
name|BranchNameKey
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
name|lib
operator|.
name|ObjectId
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
name|lib
operator|.
name|Ref
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
name|lib
operator|.
name|Repository
import|;
end_import

begin_comment
comment|/** Cache for mergeability of commits into destination branches. */
end_comment

begin_interface
DECL|interface|MergeabilityCache
specifier|public
interface|interface
name|MergeabilityCache
block|{
DECL|class|NotImplemented
class|class
name|NotImplemented
implements|implements
name|MergeabilityCache
block|{
annotation|@
name|Override
DECL|method|get ( ObjectId commit, Ref intoRef, SubmitType submitType, String mergeStrategy, BranchNameKey dest, Repository repo)
specifier|public
name|boolean
name|get
parameter_list|(
name|ObjectId
name|commit
parameter_list|,
name|Ref
name|intoRef
parameter_list|,
name|SubmitType
name|submitType
parameter_list|,
name|String
name|mergeStrategy
parameter_list|,
name|BranchNameKey
name|dest
parameter_list|,
name|Repository
name|repo
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Mergeability checking disabled"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|getIfPresent ( ObjectId commit, Ref intoRef, SubmitType submitType, String mergeStrategy)
specifier|public
name|Boolean
name|getIfPresent
parameter_list|(
name|ObjectId
name|commit
parameter_list|,
name|Ref
name|intoRef
parameter_list|,
name|SubmitType
name|submitType
parameter_list|,
name|String
name|mergeStrategy
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Mergeability checking disabled"
argument_list|)
throw|;
block|}
block|}
DECL|method|get ( ObjectId commit, Ref intoRef, SubmitType submitType, String mergeStrategy, BranchNameKey dest, Repository repo)
name|boolean
name|get
parameter_list|(
name|ObjectId
name|commit
parameter_list|,
name|Ref
name|intoRef
parameter_list|,
name|SubmitType
name|submitType
parameter_list|,
name|String
name|mergeStrategy
parameter_list|,
name|BranchNameKey
name|dest
parameter_list|,
name|Repository
name|repo
parameter_list|)
function_decl|;
DECL|method|getIfPresent (ObjectId commit, Ref intoRef, SubmitType submitType, String mergeStrategy)
name|Boolean
name|getIfPresent
parameter_list|(
name|ObjectId
name|commit
parameter_list|,
name|Ref
name|intoRef
parameter_list|,
name|SubmitType
name|submitType
parameter_list|,
name|String
name|mergeStrategy
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

