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
DECL|package|com.google.gerrit.reviewdb.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
operator|.
name|InheritableBoolean
import|;
end_import

begin_class
DECL|class|InheritedBoolean
specifier|public
class|class
name|InheritedBoolean
block|{
DECL|field|value
specifier|public
name|InheritableBoolean
name|value
decl_stmt|;
DECL|field|inheritedValue
specifier|public
name|boolean
name|inheritedValue
decl_stmt|;
DECL|method|InheritedBoolean ()
specifier|public
name|InheritedBoolean
parameter_list|()
block|{   }
DECL|method|setValue (final InheritableBoolean value)
specifier|public
name|void
name|setValue
parameter_list|(
specifier|final
name|InheritableBoolean
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
DECL|method|setInheritedValue (final boolean inheritedValue)
specifier|public
name|void
name|setInheritedValue
parameter_list|(
specifier|final
name|boolean
name|inheritedValue
parameter_list|)
block|{
name|this
operator|.
name|inheritedValue
operator|=
name|inheritedValue
expr_stmt|;
block|}
block|}
end_class

end_unit

