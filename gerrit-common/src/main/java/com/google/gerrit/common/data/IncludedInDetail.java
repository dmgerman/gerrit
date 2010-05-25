begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
name|java
operator|.
name|util
operator|.
name|Collections
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
DECL|class|IncludedInDetail
specifier|public
class|class
name|IncludedInDetail
block|{
DECL|field|branches
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|branches
decl_stmt|;
DECL|field|tags
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|tags
decl_stmt|;
DECL|method|IncludedInDetail ()
specifier|public
name|IncludedInDetail
parameter_list|()
block|{   }
DECL|method|setBranches (final List<String> b)
specifier|public
name|void
name|setBranches
parameter_list|(
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|b
parameter_list|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|branches
operator|=
name|b
expr_stmt|;
block|}
DECL|method|getBranches ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getBranches
parameter_list|()
block|{
return|return
name|branches
return|;
block|}
DECL|method|setTags (final List<String> t)
specifier|public
name|void
name|setTags
parameter_list|(
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|t
parameter_list|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|tags
operator|=
name|t
expr_stmt|;
block|}
DECL|method|getTags ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getTags
parameter_list|()
block|{
return|return
name|tags
return|;
block|}
block|}
end_class

end_unit

