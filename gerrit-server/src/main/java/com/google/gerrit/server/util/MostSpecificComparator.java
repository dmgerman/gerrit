begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|util
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
name|common
operator|.
name|data
operator|.
name|RefConfigSection
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
name|project
operator|.
name|RefPattern
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  * Order the Ref Pattern by the most specific. This sort is done by:  *  *<ul>  *<li>1 - The minor value of Levenshtein string distance between the branch name and the regex  *       string shortest example. A shorter distance is a more specific match.  *<li>2 - Finites first, infinities after.  *<li>3 - Number of transitions. More transitions is more specific.  *<li>4 - Length of the expression text.  *</ul>  *  * Levenshtein distance is a measure of the similarity between two strings. The distance is the  * number of deletions, insertions, or substitutions required to transform one string into another.  *  *<p>For example, if given refs/heads/m* and refs/heads/*, the distances are 5 and 6. It means that  * refs/heads/m* is more specific because it's closer to refs/heads/master than refs/heads/*.  *  *<p>Another example could be refs/heads/* and refs/heads/[a-zA-Z]*, the distances are both 6. Both  * are infinite, but refs/heads/[a-zA-Z]* has more transitions, which after all turns it more  * specific.  */
end_comment

begin_class
DECL|class|MostSpecificComparator
specifier|public
specifier|final
class|class
name|MostSpecificComparator
implements|implements
name|Comparator
argument_list|<
name|RefConfigSection
argument_list|>
block|{
DECL|field|refName
specifier|private
specifier|final
name|String
name|refName
decl_stmt|;
DECL|method|MostSpecificComparator (String refName)
specifier|public
name|MostSpecificComparator
parameter_list|(
name|String
name|refName
parameter_list|)
block|{
name|this
operator|.
name|refName
operator|=
name|refName
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|compare (RefConfigSection a, RefConfigSection b)
specifier|public
name|int
name|compare
parameter_list|(
name|RefConfigSection
name|a
parameter_list|,
name|RefConfigSection
name|b
parameter_list|)
block|{
return|return
name|compare
argument_list|(
name|a
operator|.
name|getName
argument_list|()
argument_list|,
name|b
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
DECL|method|compare (final String pattern1, final String pattern2)
specifier|public
name|int
name|compare
parameter_list|(
specifier|final
name|String
name|pattern1
parameter_list|,
specifier|final
name|String
name|pattern2
parameter_list|)
block|{
name|int
name|cmp
init|=
name|distance
argument_list|(
name|pattern1
argument_list|)
operator|-
name|distance
argument_list|(
name|pattern2
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmp
operator|==
literal|0
condition|)
block|{
name|boolean
name|p1_finite
init|=
name|finite
argument_list|(
name|pattern1
argument_list|)
decl_stmt|;
name|boolean
name|p2_finite
init|=
name|finite
argument_list|(
name|pattern2
argument_list|)
decl_stmt|;
if|if
condition|(
name|p1_finite
operator|&&
operator|!
name|p2_finite
condition|)
block|{
name|cmp
operator|=
operator|-
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|p1_finite
operator|&&
name|p2_finite
condition|)
block|{
name|cmp
operator|=
literal|1
expr_stmt|;
block|}
else|else
comment|/* if (f1 == f2) */
block|{
name|cmp
operator|=
literal|0
expr_stmt|;
block|}
block|}
if|if
condition|(
name|cmp
operator|==
literal|0
condition|)
block|{
name|cmp
operator|=
name|transitions
argument_list|(
name|pattern2
argument_list|)
operator|-
name|transitions
argument_list|(
name|pattern1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cmp
operator|==
literal|0
condition|)
block|{
name|cmp
operator|=
name|pattern2
operator|.
name|length
argument_list|()
operator|-
name|pattern1
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
return|return
name|cmp
return|;
block|}
DECL|method|distance (String pattern)
specifier|private
name|int
name|distance
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|String
name|example
decl_stmt|;
if|if
condition|(
name|RefPattern
operator|.
name|isRE
argument_list|(
name|pattern
argument_list|)
condition|)
block|{
name|example
operator|=
name|RefPattern
operator|.
name|shortestExample
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|pattern
operator|.
name|endsWith
argument_list|(
literal|"/*"
argument_list|)
condition|)
block|{
name|example
operator|=
name|pattern
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|pattern
operator|.
name|equals
argument_list|(
name|refName
argument_list|)
condition|)
block|{
return|return
literal|0
return|;
block|}
else|else
block|{
return|return
name|Math
operator|.
name|max
argument_list|(
name|pattern
operator|.
name|length
argument_list|()
argument_list|,
name|refName
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
return|return
name|StringUtils
operator|.
name|getLevenshteinDistance
argument_list|(
name|example
argument_list|,
name|refName
argument_list|)
return|;
block|}
DECL|method|finite (String pattern)
specifier|private
name|boolean
name|finite
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
if|if
condition|(
name|RefPattern
operator|.
name|isRE
argument_list|(
name|pattern
argument_list|)
condition|)
block|{
return|return
name|RefPattern
operator|.
name|toRegExp
argument_list|(
name|pattern
argument_list|)
operator|.
name|toAutomaton
argument_list|()
operator|.
name|isFinite
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|pattern
operator|.
name|endsWith
argument_list|(
literal|"/*"
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
return|return
literal|true
return|;
block|}
block|}
DECL|method|transitions (String pattern)
specifier|private
name|int
name|transitions
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
if|if
condition|(
name|RefPattern
operator|.
name|isRE
argument_list|(
name|pattern
argument_list|)
condition|)
block|{
return|return
name|RefPattern
operator|.
name|toRegExp
argument_list|(
name|pattern
argument_list|)
operator|.
name|toAutomaton
argument_list|()
operator|.
name|getNumberOfTransitions
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|pattern
operator|.
name|endsWith
argument_list|(
literal|"/*"
argument_list|)
condition|)
block|{
return|return
name|pattern
operator|.
name|length
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|pattern
operator|.
name|length
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

