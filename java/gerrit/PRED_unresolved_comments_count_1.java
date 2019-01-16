begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|gerrit
package|package
name|gerrit
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
name|exceptions
operator|.
name|StorageException
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
name|rules
operator|.
name|StoredValues
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|exceptions
operator|.
name|JavaException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|exceptions
operator|.
name|PrologException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|IntegerTerm
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|Operation
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|Predicate
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|Prolog
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|Term
import|;
end_import

begin_class
DECL|class|PRED_unresolved_comments_count_1
specifier|public
class|class
name|PRED_unresolved_comments_count_1
extends|extends
name|Predicate
operator|.
name|P1
block|{
DECL|method|PRED_unresolved_comments_count_1 (Term a1, Operation n)
specifier|public
name|PRED_unresolved_comments_count_1
parameter_list|(
name|Term
name|a1
parameter_list|,
name|Operation
name|n
parameter_list|)
block|{
name|arg1
operator|=
name|a1
expr_stmt|;
name|cont
operator|=
name|n
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|exec (Prolog engine)
specifier|public
name|Operation
name|exec
parameter_list|(
name|Prolog
name|engine
parameter_list|)
throws|throws
name|PrologException
block|{
name|engine
operator|.
name|setB0
argument_list|()
expr_stmt|;
name|Term
name|a1
init|=
name|arg1
operator|.
name|dereference
argument_list|()
decl_stmt|;
try|try
block|{
name|Integer
name|count
init|=
name|StoredValues
operator|.
name|CHANGE_DATA
operator|.
name|get
argument_list|(
name|engine
argument_list|)
operator|.
name|unresolvedCommentCount
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|a1
operator|.
name|unify
argument_list|(
operator|new
name|IntegerTerm
argument_list|(
name|count
operator|!=
literal|null
condition|?
name|count
else|:
literal|0
argument_list|)
argument_list|,
name|engine
operator|.
name|trail
argument_list|)
condition|)
block|{
return|return
name|engine
operator|.
name|fail
argument_list|()
return|;
block|}
block|}
catch|catch
parameter_list|(
name|StorageException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|JavaException
argument_list|(
name|this
argument_list|,
literal|1
argument_list|,
name|err
argument_list|)
throw|;
block|}
return|return
name|cont
return|;
block|}
block|}
end_class

end_unit

