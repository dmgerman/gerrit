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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
package|;
end_package

begin_class
DECL|class|AddBranchResult
specifier|public
class|class
name|AddBranchResult
block|{
DECL|field|listBranchesResult
specifier|protected
name|ListBranchesResult
name|listBranchesResult
decl_stmt|;
DECL|field|error
specifier|protected
name|Error
name|error
decl_stmt|;
DECL|method|AddBranchResult ()
specifier|protected
name|AddBranchResult
parameter_list|()
block|{   }
DECL|method|AddBranchResult (final Error error)
specifier|public
name|AddBranchResult
parameter_list|(
specifier|final
name|Error
name|error
parameter_list|)
block|{
name|this
operator|.
name|error
operator|=
name|error
expr_stmt|;
block|}
DECL|method|AddBranchResult (final ListBranchesResult listBranchesResult)
specifier|public
name|AddBranchResult
parameter_list|(
specifier|final
name|ListBranchesResult
name|listBranchesResult
parameter_list|)
block|{
name|this
operator|.
name|listBranchesResult
operator|=
name|listBranchesResult
expr_stmt|;
block|}
DECL|method|getListBranchesResult ()
specifier|public
name|ListBranchesResult
name|getListBranchesResult
parameter_list|()
block|{
return|return
name|listBranchesResult
return|;
block|}
DECL|method|hasError ()
specifier|public
name|boolean
name|hasError
parameter_list|()
block|{
return|return
name|error
operator|!=
literal|null
return|;
block|}
DECL|method|getError ()
specifier|public
name|Error
name|getError
parameter_list|()
block|{
return|return
name|error
return|;
block|}
DECL|class|Error
specifier|public
specifier|static
class|class
name|Error
block|{
DECL|enum|Type
specifier|public
specifier|static
enum|enum
name|Type
block|{
comment|/** The branch cannot be created because the given branch name is invalid. */
DECL|enumConstant|INVALID_NAME
name|INVALID_NAME
block|,
comment|/** The branch cannot be created because the given revision is invalid. */
DECL|enumConstant|INVALID_REVISION
name|INVALID_REVISION
block|,
comment|/**        * The branch cannot be created under the given refname prefix (e.g        * branches cannot be created under magic refname prefixes).        */
DECL|enumConstant|BRANCH_CREATION_NOT_ALLOWED_UNDER_REFNAME_PREFIX
name|BRANCH_CREATION_NOT_ALLOWED_UNDER_REFNAME_PREFIX
block|,
comment|/** The branch that should be created exists already. */
DECL|enumConstant|BRANCH_ALREADY_EXISTS
name|BRANCH_ALREADY_EXISTS
block|,
comment|/**        * The branch cannot be created because it conflicts with an existing        * branch (branches cannot be nested).        */
DECL|enumConstant|BRANCH_CREATION_CONFLICT
name|BRANCH_CREATION_CONFLICT
block|}
DECL|field|type
specifier|protected
name|Type
name|type
decl_stmt|;
DECL|field|refname
specifier|protected
name|String
name|refname
decl_stmt|;
DECL|method|Error ()
specifier|protected
name|Error
parameter_list|()
block|{     }
DECL|method|Error (final Type type)
specifier|public
name|Error
parameter_list|(
specifier|final
name|Type
name|type
parameter_list|)
block|{
name|this
argument_list|(
name|type
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|Error (final Type type, final String refname)
specifier|public
name|Error
parameter_list|(
specifier|final
name|Type
name|type
parameter_list|,
specifier|final
name|String
name|refname
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|refname
operator|=
name|refname
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|Type
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|getRefname ()
specifier|public
name|String
name|getRefname
parameter_list|()
block|{
return|return
name|refname
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|type
operator|+
literal|" "
operator|+
name|refname
return|;
block|}
block|}
block|}
end_class

end_unit

