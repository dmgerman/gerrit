begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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

begin_class
annotation|@
name|AutoValue
DECL|class|LabelId
specifier|public
specifier|abstract
class|class
name|LabelId
block|{
DECL|field|LEGACY_SUBMIT_NAME
specifier|static
specifier|final
name|String
name|LEGACY_SUBMIT_NAME
init|=
literal|"SUBM"
decl_stmt|;
DECL|method|create (String n)
specifier|public
specifier|static
name|LabelId
name|create
parameter_list|(
name|String
name|n
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_LabelId
argument_list|(
name|n
argument_list|)
return|;
block|}
DECL|method|legacySubmit ()
specifier|public
specifier|static
name|LabelId
name|legacySubmit
parameter_list|()
block|{
return|return
name|create
argument_list|(
name|LEGACY_SUBMIT_NAME
argument_list|)
return|;
block|}
DECL|method|id ()
specifier|abstract
name|String
name|id
parameter_list|()
function_decl|;
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|id
argument_list|()
return|;
block|}
block|}
end_class

end_unit

