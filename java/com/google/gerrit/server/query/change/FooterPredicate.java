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
name|gerrit
operator|.
name|server
operator|.
name|index
operator|.
name|change
operator|.
name|ChangeField
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_class
DECL|class|FooterPredicate
specifier|public
class|class
name|FooterPredicate
extends|extends
name|ChangeIndexPredicate
block|{
DECL|method|clean (String value)
specifier|private
specifier|static
name|String
name|clean
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|int
name|indexEquals
init|=
name|value
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
decl_stmt|;
name|int
name|indexColon
init|=
name|value
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
comment|// footer key cannot contain '='
if|if
condition|(
name|indexEquals
operator|>
literal|0
operator|&&
operator|(
name|indexEquals
operator|<
name|indexColon
operator|||
name|indexColon
operator|<
literal|0
operator|)
condition|)
block|{
name|value
operator|=
name|value
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|indexEquals
argument_list|)
operator|+
literal|": "
operator|+
name|value
operator|.
name|substring
argument_list|(
name|indexEquals
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|value
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
return|;
block|}
DECL|method|FooterPredicate (String value)
name|FooterPredicate
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|ChangeField
operator|.
name|FOOTER
argument_list|,
name|clean
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|match (ChangeData cd)
specifier|public
name|boolean
name|match
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
block|{
return|return
name|ChangeField
operator|.
name|getFooters
argument_list|(
name|cd
argument_list|)
operator|.
name|contains
argument_list|(
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getCost ()
specifier|public
name|int
name|getCost
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

