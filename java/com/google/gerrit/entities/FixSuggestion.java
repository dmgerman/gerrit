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
DECL|package|com.google.gerrit.entities
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
package|;
end_package

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
DECL|class|FixSuggestion
specifier|public
class|class
name|FixSuggestion
block|{
DECL|field|fixId
specifier|public
name|String
name|fixId
decl_stmt|;
DECL|field|description
specifier|public
name|String
name|description
decl_stmt|;
DECL|field|replacements
specifier|public
name|List
argument_list|<
name|FixReplacement
argument_list|>
name|replacements
decl_stmt|;
DECL|method|FixSuggestion (String fixId, String description, List<FixReplacement> replacements)
specifier|public
name|FixSuggestion
parameter_list|(
name|String
name|fixId
parameter_list|,
name|String
name|description
parameter_list|,
name|List
argument_list|<
name|FixReplacement
argument_list|>
name|replacements
parameter_list|)
block|{
name|this
operator|.
name|fixId
operator|=
name|fixId
expr_stmt|;
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
name|this
operator|.
name|replacements
operator|=
name|replacements
expr_stmt|;
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
literal|"FixSuggestion{"
operator|+
literal|"fixId='"
operator|+
name|fixId
operator|+
literal|'\''
operator|+
literal|", description='"
operator|+
name|description
operator|+
literal|'\''
operator|+
literal|", replacements="
operator|+
name|replacements
operator|+
literal|'}'
return|;
block|}
block|}
end_class

end_unit

