begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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

begin_class
DECL|class|LabelInfo
specifier|public
class|class
name|LabelInfo
block|{
DECL|field|approved
specifier|public
name|AccountInfo
name|approved
decl_stmt|;
DECL|field|rejected
specifier|public
name|AccountInfo
name|rejected
decl_stmt|;
DECL|field|recommended
specifier|public
name|AccountInfo
name|recommended
decl_stmt|;
DECL|field|disliked
specifier|public
name|AccountInfo
name|disliked
decl_stmt|;
DECL|field|all
specifier|public
name|List
argument_list|<
name|ApprovalInfo
argument_list|>
name|all
decl_stmt|;
DECL|field|values
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
decl_stmt|;
DECL|field|value
specifier|public
name|Short
name|value
decl_stmt|;
DECL|field|defaultValue
specifier|public
name|Short
name|defaultValue
decl_stmt|;
DECL|field|optional
specifier|public
name|Boolean
name|optional
decl_stmt|;
DECL|field|blocking
specifier|public
name|Boolean
name|blocking
decl_stmt|;
block|}
end_class

end_unit

