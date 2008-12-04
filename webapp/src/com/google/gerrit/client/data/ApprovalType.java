begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|client
operator|.
name|reviewdb
operator|.
name|ApprovalCategory
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
name|client
operator|.
name|reviewdb
operator|.
name|ApprovalCategoryValue
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

begin_class
DECL|class|ApprovalType
specifier|public
class|class
name|ApprovalType
block|{
DECL|field|category
specifier|private
name|ApprovalCategory
name|category
decl_stmt|;
DECL|field|values
specifier|private
name|List
argument_list|<
name|ApprovalCategoryValue
argument_list|>
name|values
decl_stmt|;
DECL|method|ApprovalType ()
specifier|protected
name|ApprovalType
parameter_list|()
block|{   }
DECL|method|ApprovalType (final ApprovalCategory ac, final List<ApprovalCategoryValue> valueList)
specifier|public
name|ApprovalType
parameter_list|(
specifier|final
name|ApprovalCategory
name|ac
parameter_list|,
specifier|final
name|List
argument_list|<
name|ApprovalCategoryValue
argument_list|>
name|valueList
parameter_list|)
block|{
name|category
operator|=
name|ac
expr_stmt|;
name|values
operator|=
operator|new
name|ArrayList
argument_list|<
name|ApprovalCategoryValue
argument_list|>
argument_list|(
name|valueList
argument_list|)
expr_stmt|;
block|}
DECL|method|getCategory ()
specifier|public
name|ApprovalCategory
name|getCategory
parameter_list|()
block|{
return|return
name|category
return|;
block|}
DECL|method|getValues ()
specifier|public
name|List
argument_list|<
name|ApprovalCategoryValue
argument_list|>
name|getValues
parameter_list|()
block|{
return|return
name|values
return|;
block|}
block|}
end_class

end_unit

