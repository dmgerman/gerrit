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
DECL|package|com.google.gerrit.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
package|;
end_package

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
name|Constants
import|;
end_import

begin_class
DECL|class|ProjectUtil
specifier|public
class|class
name|ProjectUtil
block|{
DECL|method|stripGitSuffix (String name)
specifier|public
specifier|static
name|String
name|stripGitSuffix
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|nameWithoutSuffix
init|=
name|name
decl_stmt|;
if|if
condition|(
name|nameWithoutSuffix
operator|.
name|endsWith
argument_list|(
name|Constants
operator|.
name|DOT_GIT_EXT
argument_list|)
condition|)
block|{
comment|// Be nice and drop the trailing ".git" suffix, which we never keep
comment|// in our database, but clients might mistakenly provide anyway.
comment|//
name|nameWithoutSuffix
operator|=
name|nameWithoutSuffix
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
comment|//
name|nameWithoutSuffix
operator|.
name|length
argument_list|()
operator|-
name|Constants
operator|.
name|DOT_GIT_EXT
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
while|while
condition|(
name|nameWithoutSuffix
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|nameWithoutSuffix
operator|=
name|nameWithoutSuffix
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|nameWithoutSuffix
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|nameWithoutSuffix
return|;
block|}
block|}
end_class

end_unit

