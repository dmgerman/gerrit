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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|Iterables
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
name|common
operator|.
name|data
operator|.
name|GroupReference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Utility class for dealing with a GroupBackend.  */
end_comment

begin_class
DECL|class|GroupBackends
specifier|public
class|class
name|GroupBackends
block|{
DECL|field|GROUP_REF_NAME_COMPARATOR
specifier|public
specifier|static
specifier|final
name|Comparator
argument_list|<
name|GroupReference
argument_list|>
name|GROUP_REF_NAME_COMPARATOR
init|=
operator|new
name|Comparator
argument_list|<
name|GroupReference
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|GroupReference
name|a
parameter_list|,
name|GroupReference
name|b
parameter_list|)
block|{
return|return
name|a
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
decl_stmt|;
comment|/**    * Runs {@link GroupBackend#suggest(String)} and filters the result to return    * the best suggestion, or null if one does not exist.    *    * @param groupBackend the group backend    * @param name the name for which to suggest groups    * @return the best single GroupReference suggestion    */
annotation|@
name|Nullable
DECL|method|findBestSuggestion ( GroupBackend groupBackend, String name)
specifier|public
specifier|static
name|GroupReference
name|findBestSuggestion
parameter_list|(
name|GroupBackend
name|groupBackend
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|Collection
argument_list|<
name|GroupReference
argument_list|>
name|refs
init|=
name|groupBackend
operator|.
name|suggest
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|refs
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|refs
argument_list|)
return|;
block|}
for|for
control|(
name|GroupReference
name|ref
range|:
name|refs
control|)
block|{
if|if
condition|(
name|isExactSuggestion
argument_list|(
name|ref
argument_list|,
name|name
argument_list|)
condition|)
block|{
return|return
name|ref
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**    * Runs {@link GroupBackend#suggest(String)} and filters the result to return    * the exact suggestion, or null if one does not exist.    *    * @param groupBackend the group backend    * @param name the name for which to suggest groups    * @return the exact single GroupReference suggestion    */
annotation|@
name|Nullable
DECL|method|findExactSuggestion ( GroupBackend groupBackend, String name)
specifier|public
specifier|static
name|GroupReference
name|findExactSuggestion
parameter_list|(
name|GroupBackend
name|groupBackend
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|Collection
argument_list|<
name|GroupReference
argument_list|>
name|refs
init|=
name|groupBackend
operator|.
name|suggest
argument_list|(
name|name
argument_list|)
decl_stmt|;
for|for
control|(
name|GroupReference
name|ref
range|:
name|refs
control|)
block|{
if|if
condition|(
name|isExactSuggestion
argument_list|(
name|ref
argument_list|,
name|name
argument_list|)
condition|)
block|{
return|return
name|ref
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/** Returns whether the GroupReference is an exact suggestion for the name. */
DECL|method|isExactSuggestion (GroupReference ref, String name)
specifier|public
specifier|static
name|boolean
name|isExactSuggestion
parameter_list|(
name|GroupReference
name|ref
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|ref
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|name
argument_list|)
operator|||
name|ref
operator|.
name|getUUID
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|GroupBackends ()
specifier|private
name|GroupBackends
parameter_list|()
block|{   }
block|}
end_class

end_unit

