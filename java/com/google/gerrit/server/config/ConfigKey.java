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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
package|;
end_package

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
name|gerrit
operator|.
name|common
operator|.
name|Nullable
import|;
end_import

begin_class
annotation|@
name|AutoValue
DECL|class|ConfigKey
specifier|public
specifier|abstract
class|class
name|ConfigKey
block|{
DECL|method|section ()
specifier|public
specifier|abstract
name|String
name|section
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|subsection ()
specifier|public
specifier|abstract
name|String
name|subsection
parameter_list|()
function_decl|;
DECL|method|name ()
specifier|public
specifier|abstract
name|String
name|name
parameter_list|()
function_decl|;
DECL|method|create (String section, String subsection, String name)
specifier|public
specifier|static
name|ConfigKey
name|create
parameter_list|(
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_ConfigKey
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|)
return|;
block|}
DECL|method|create (String section, String name)
specifier|public
specifier|static
name|ConfigKey
name|create
parameter_list|(
name|String
name|section
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_ConfigKey
argument_list|(
name|section
argument_list|,
literal|null
argument_list|,
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
specifier|final
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|section
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
if|if
condition|(
name|subsection
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|subsection
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|name
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

