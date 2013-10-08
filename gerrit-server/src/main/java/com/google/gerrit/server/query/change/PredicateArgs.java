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
DECL|package|com.google.gerrit.server.query.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
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
name|common
operator|.
name|collect
operator|.
name|Lists
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Maps
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
name|query
operator|.
name|QueryParseException
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * This class is used to extract comma separated values in a predicate.  *<p>  * If tags for the values are present (e.g. "branch=jb_2.3,vote=approved") then  * the args are placed in a map that maps tag to value (e.g., "branch" to "jb_2.3").  * If no tag is present (e.g. "jb_2.3,approved") then the args are placed into a  * positional list.  Args may be mixed so some may appear in the map and others  * in the positional list (e.g. "vote=approved,jb_2.3).  */
end_comment

begin_class
DECL|class|PredicateArgs
specifier|public
class|class
name|PredicateArgs
block|{
DECL|field|positional
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|positional
decl_stmt|;
DECL|field|keyValue
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|keyValue
decl_stmt|;
comment|/**    * Parses query arguments into {@link #keyValue} and/or {@link #positional}..    *<p>    * Labels for these arguments should be kept in ChangeQueryBuilder    * as {@code ARG_ID_{argument name}}.    *    * @param args arguments to be parsed    * @throws QueryParseException    */
DECL|method|PredicateArgs (String args)
name|PredicateArgs
parameter_list|(
name|String
name|args
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|positional
operator|=
name|Lists
operator|.
name|newArrayList
argument_list|()
expr_stmt|;
name|keyValue
operator|=
name|Maps
operator|.
name|newHashMap
argument_list|()
expr_stmt|;
name|String
index|[]
name|splitArgs
init|=
name|args
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|arg
range|:
name|splitArgs
control|)
block|{
name|String
index|[]
name|splitKeyValue
init|=
name|arg
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
if|if
condition|(
name|splitKeyValue
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|positional
operator|.
name|add
argument_list|(
name|splitKeyValue
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|splitKeyValue
operator|.
name|length
operator|==
literal|2
condition|)
block|{
if|if
condition|(
operator|!
name|keyValue
operator|.
name|containsKey
argument_list|(
name|splitKeyValue
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
name|keyValue
operator|.
name|put
argument_list|(
name|splitKeyValue
index|[
literal|0
index|]
argument_list|,
name|splitKeyValue
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|QueryParseException
argument_list|(
literal|"Duplicate key "
operator|+
name|splitKeyValue
index|[
literal|0
index|]
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|QueryParseException
argument_list|(
literal|"invalid arg "
operator|+
name|arg
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

