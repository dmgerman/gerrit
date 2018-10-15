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
DECL|package|com.google.gerrit.extensions.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|TestSubmitRuleInfo
specifier|public
class|class
name|TestSubmitRuleInfo
block|{
comment|/** @see com.google.gerrit.common.data.SubmitRecord.Status */
DECL|field|status
specifier|public
name|String
name|status
decl_stmt|;
DECL|field|errorMessage
specifier|public
name|String
name|errorMessage
decl_stmt|;
DECL|field|ok
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|AccountInfo
argument_list|>
name|ok
decl_stmt|;
DECL|field|reject
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|AccountInfo
argument_list|>
name|reject
decl_stmt|;
DECL|field|need
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|None
argument_list|>
name|need
decl_stmt|;
DECL|field|may
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|AccountInfo
argument_list|>
name|may
decl_stmt|;
DECL|field|impossible
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|None
argument_list|>
name|impossible
decl_stmt|;
DECL|class|None
specifier|public
specifier|static
class|class
name|None
block|{
DECL|method|None ()
specifier|private
name|None
parameter_list|()
block|{}
DECL|field|INSTANCE
specifier|public
specifier|static
name|None
name|INSTANCE
init|=
operator|new
name|None
argument_list|()
decl_stmt|;
block|}
block|}
end_class

end_unit

