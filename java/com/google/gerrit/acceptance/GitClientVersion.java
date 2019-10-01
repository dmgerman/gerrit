begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|joining
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|IntStream
import|;
end_import

begin_comment
comment|/** Class to parse and represent version of git-core client */
end_comment

begin_class
DECL|class|GitClientVersion
specifier|public
class|class
name|GitClientVersion
implements|implements
name|Comparable
argument_list|<
name|GitClientVersion
argument_list|>
block|{
DECL|field|v
specifier|private
specifier|final
name|int
name|v
index|[]
decl_stmt|;
comment|/**    * Constructor to represent instance for minimum supported git-core version    *    * @param parts version passed as single digits    */
DECL|method|GitClientVersion (int... parts)
specifier|public
name|GitClientVersion
parameter_list|(
name|int
modifier|...
name|parts
parameter_list|)
block|{
name|this
operator|.
name|v
operator|=
name|parts
expr_stmt|;
block|}
comment|/**    * Parse the git-core version as returned by git version command    *    * @param version String returned by git version command    */
DECL|method|GitClientVersion (String version)
specifier|public
name|GitClientVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
comment|// "git version x.y.z"
name|String
name|parts
index|[]
init|=
name|version
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
index|[
literal|2
index|]
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
decl_stmt|;
name|v
operator|=
operator|new
name|int
index|[
name|parts
operator|.
name|length
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|parts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|v
index|[
name|i
index|]
operator|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|parts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|compareTo (GitClientVersion o)
specifier|public
name|int
name|compareTo
parameter_list|(
name|GitClientVersion
name|o
parameter_list|)
block|{
name|int
name|m
init|=
name|Math
operator|.
name|max
argument_list|(
name|v
operator|.
name|length
argument_list|,
name|o
operator|.
name|v
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|m
condition|;
name|i
operator|++
control|)
block|{
name|int
name|l
init|=
name|i
operator|<
name|v
operator|.
name|length
condition|?
name|v
index|[
name|i
index|]
else|:
literal|0
decl_stmt|;
name|int
name|r
init|=
name|i
operator|<
name|o
operator|.
name|v
operator|.
name|length
condition|?
name|o
operator|.
name|v
index|[
name|i
index|]
else|:
literal|0
decl_stmt|;
if|if
condition|(
name|l
operator|!=
name|r
condition|)
block|{
return|return
name|l
operator|<
name|r
condition|?
operator|-
literal|1
else|:
literal|1
return|;
block|}
block|}
return|return
literal|0
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
name|IntStream
operator|.
name|of
argument_list|(
name|v
argument_list|)
operator|.
name|mapToObj
argument_list|(
name|String
operator|::
name|valueOf
argument_list|)
operator|.
name|collect
argument_list|(
name|joining
argument_list|(
literal|"."
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

