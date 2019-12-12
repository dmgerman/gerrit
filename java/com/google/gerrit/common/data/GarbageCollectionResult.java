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
name|Project
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_comment
comment|/** A list of errors occurred during GC. */
end_comment

begin_class
DECL|class|GarbageCollectionResult
specifier|public
class|class
name|GarbageCollectionResult
block|{
DECL|field|errors
specifier|protected
name|List
argument_list|<
name|Error
argument_list|>
name|errors
decl_stmt|;
DECL|method|GarbageCollectionResult ()
specifier|public
name|GarbageCollectionResult
parameter_list|()
block|{
name|errors
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
DECL|method|addError (Error e)
specifier|public
name|void
name|addError
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
name|errors
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
DECL|method|getErrors ()
specifier|public
name|List
argument_list|<
name|Error
argument_list|>
name|getErrors
parameter_list|()
block|{
return|return
name|errors
return|;
block|}
DECL|method|hasErrors ()
specifier|public
name|boolean
name|hasErrors
parameter_list|()
block|{
return|return
operator|!
name|errors
operator|.
name|isEmpty
argument_list|()
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
enum|enum
name|Type
block|{
comment|/** Git garbage collection was already scheduled for this project */
DECL|enumConstant|GC_ALREADY_SCHEDULED
name|GC_ALREADY_SCHEDULED
block|,
comment|/** The repository was not found. */
DECL|enumConstant|REPOSITORY_NOT_FOUND
name|REPOSITORY_NOT_FOUND
block|,
comment|/** The Git garbage collection failed. */
DECL|enumConstant|GC_FAILED
name|GC_FAILED
block|}
DECL|field|type
specifier|protected
name|Type
name|type
decl_stmt|;
DECL|field|projectName
specifier|protected
name|Project
operator|.
name|NameKey
name|projectName
decl_stmt|;
DECL|method|Error ()
specifier|protected
name|Error
parameter_list|()
block|{}
DECL|method|Error (Type type, Project.NameKey projectName)
specifier|public
name|Error
parameter_list|(
name|Type
name|type
parameter_list|,
name|Project
operator|.
name|NameKey
name|projectName
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
name|projectName
operator|=
name|projectName
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
DECL|method|getProjectName ()
specifier|public
name|Project
operator|.
name|NameKey
name|getProjectName
parameter_list|()
block|{
return|return
name|projectName
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
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
name|type
argument_list|)
expr_stmt|;
if|if
condition|(
name|projectName
operator|!=
literal|null
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|projectName
argument_list|)
expr_stmt|;
block|}
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

