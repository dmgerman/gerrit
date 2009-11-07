begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|AccountProjectWatch
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
name|reviewdb
operator|.
name|Project
import|;
end_import

begin_class
DECL|class|AccountProjectWatchInfo
specifier|public
specifier|final
class|class
name|AccountProjectWatchInfo
block|{
DECL|field|watch
specifier|protected
name|AccountProjectWatch
name|watch
decl_stmt|;
DECL|field|project
specifier|protected
name|Project
name|project
decl_stmt|;
DECL|method|AccountProjectWatchInfo ()
specifier|protected
name|AccountProjectWatchInfo
parameter_list|()
block|{   }
DECL|method|AccountProjectWatchInfo (final AccountProjectWatch w, final Project p)
specifier|public
name|AccountProjectWatchInfo
parameter_list|(
specifier|final
name|AccountProjectWatch
name|w
parameter_list|,
specifier|final
name|Project
name|p
parameter_list|)
block|{
name|watch
operator|=
name|w
expr_stmt|;
name|project
operator|=
name|p
expr_stmt|;
block|}
DECL|method|getWatch ()
specifier|public
name|AccountProjectWatch
name|getWatch
parameter_list|()
block|{
return|return
name|watch
return|;
block|}
DECL|method|getProject ()
specifier|public
name|Project
name|getProject
parameter_list|()
block|{
return|return
name|project
return|;
block|}
block|}
end_class

end_unit

