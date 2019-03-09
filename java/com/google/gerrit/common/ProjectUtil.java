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

begin_class
DECL|class|ProjectUtil
specifier|public
class|class
name|ProjectUtil
block|{
DECL|method|sanitizeProjectName (String name)
specifier|public
specifier|static
name|String
name|sanitizeProjectName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|name
operator|=
name|stripGitSuffix
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|name
operator|=
name|stripTrailingSlash
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|name
return|;
block|}
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
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|".git"
argument_list|)
condition|)
block|{
comment|// Be nice and drop the trailing ".git" suffix, which we never keep
comment|// in our database, but clients might mistakenly provide anyway.
comment|//
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
name|name
operator|=
name|stripTrailingSlash
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
DECL|method|stripTrailingSlash (String name)
specifier|private
specifier|static
name|String
name|stripTrailingSlash
parameter_list|(
name|String
name|name
parameter_list|)
block|{
while|while
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
DECL|method|ProjectUtil ()
specifier|private
name|ProjectUtil
parameter_list|()
block|{}
block|}
end_class

end_unit

