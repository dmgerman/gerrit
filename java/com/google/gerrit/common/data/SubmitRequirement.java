begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkState
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|base
operator|.
name|CharMatcher
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
name|ImmutableMap
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
comment|/** Describes a requirement to submit a change. */
end_comment

begin_class
annotation|@
name|AutoValue
annotation|@
name|AutoValue
operator|.
name|CopyAnnotations
DECL|class|SubmitRequirement
specifier|public
specifier|abstract
class|class
name|SubmitRequirement
block|{
DECL|field|TYPE_MATCHER
specifier|private
specifier|static
specifier|final
name|CharMatcher
name|TYPE_MATCHER
init|=
name|CharMatcher
operator|.
name|inRange
argument_list|(
literal|'a'
argument_list|,
literal|'z'
argument_list|)
operator|.
name|or
argument_list|(
name|CharMatcher
operator|.
name|inRange
argument_list|(
literal|'A'
argument_list|,
literal|'Z'
argument_list|)
argument_list|)
operator|.
name|or
argument_list|(
name|CharMatcher
operator|.
name|inRange
argument_list|(
literal|'0'
argument_list|,
literal|'9'
argument_list|)
argument_list|)
operator|.
name|or
argument_list|(
name|CharMatcher
operator|.
name|anyOf
argument_list|(
literal|"-_"
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|AutoValue
operator|.
name|Builder
DECL|class|Builder
specifier|public
specifier|abstract
specifier|static
class|class
name|Builder
block|{
DECL|method|setType (String value)
specifier|public
specifier|abstract
name|Builder
name|setType
parameter_list|(
name|String
name|value
parameter_list|)
function_decl|;
DECL|method|setFallbackText (String value)
specifier|public
specifier|abstract
name|Builder
name|setFallbackText
parameter_list|(
name|String
name|value
parameter_list|)
function_decl|;
DECL|method|setData (Map<String, String> value)
specifier|public
name|Builder
name|setData
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|value
parameter_list|)
block|{
return|return
name|setData
argument_list|(
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
DECL|method|addCustomValue (String key, String value)
specifier|public
name|Builder
name|addCustomValue
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|dataBuilder
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|build ()
specifier|public
name|SubmitRequirement
name|build
parameter_list|()
block|{
name|SubmitRequirement
name|requirement
init|=
name|autoBuild
argument_list|()
decl_stmt|;
name|checkState
argument_list|(
name|validateType
argument_list|(
name|requirement
operator|.
name|type
argument_list|()
argument_list|)
argument_list|,
literal|"SubmitRequirement's type contains non alphanumerical symbols."
argument_list|)
expr_stmt|;
return|return
name|requirement
return|;
block|}
DECL|method|setData (ImmutableMap<String, String> value)
specifier|abstract
name|Builder
name|setData
parameter_list|(
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|value
parameter_list|)
function_decl|;
DECL|method|dataBuilder ()
specifier|abstract
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|dataBuilder
parameter_list|()
function_decl|;
DECL|method|autoBuild ()
specifier|abstract
name|SubmitRequirement
name|autoBuild
parameter_list|()
function_decl|;
block|}
DECL|method|fallbackText ()
specifier|public
specifier|abstract
name|String
name|fallbackText
parameter_list|()
function_decl|;
DECL|method|type ()
specifier|public
specifier|abstract
name|String
name|type
parameter_list|()
function_decl|;
DECL|method|data ()
specifier|public
specifier|abstract
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|data
parameter_list|()
function_decl|;
DECL|method|builder ()
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_SubmitRequirement
operator|.
name|Builder
argument_list|()
return|;
block|}
DECL|method|validateType (String type)
specifier|private
specifier|static
name|boolean
name|validateType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
return|return
name|TYPE_MATCHER
operator|.
name|matchesAllOf
argument_list|(
name|type
argument_list|)
return|;
block|}
block|}
end_class

end_unit

