begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertWithMessage
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|ConfigUtil
operator|.
name|skipField
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
DECL|class|AssertUtil
specifier|public
class|class
name|AssertUtil
block|{
DECL|method|assertPrefs (T actual, T expected, String... fieldsToExclude)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|assertPrefs
parameter_list|(
name|T
name|actual
parameter_list|,
name|T
name|expected
parameter_list|,
name|String
modifier|...
name|fieldsToExclude
parameter_list|)
throws|throws
name|IllegalArgumentException
throws|,
name|IllegalAccessException
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|exludedFields
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|fieldsToExclude
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Field
name|field
range|:
name|actual
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
if|if
condition|(
name|exludedFields
operator|.
name|contains
argument_list|(
name|field
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|skipField
argument_list|(
name|field
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|Object
name|actualVal
init|=
name|field
operator|.
name|get
argument_list|(
name|actual
argument_list|)
decl_stmt|;
name|Object
name|expectedVal
init|=
name|field
operator|.
name|get
argument_list|(
name|expected
argument_list|)
decl_stmt|;
if|if
condition|(
name|field
operator|.
name|getType
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|Boolean
operator|.
name|class
argument_list|)
condition|)
block|{
if|if
condition|(
name|actualVal
operator|==
literal|null
condition|)
block|{
name|actualVal
operator|=
literal|false
expr_stmt|;
block|}
if|if
condition|(
name|expectedVal
operator|==
literal|null
condition|)
block|{
name|expectedVal
operator|=
literal|false
expr_stmt|;
block|}
block|}
name|assertWithMessage
argument_list|(
name|field
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|actualVal
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedVal
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

