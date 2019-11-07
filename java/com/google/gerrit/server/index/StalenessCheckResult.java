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
DECL|package|com.google.gerrit.server.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
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
name|errorprone
operator|.
name|annotations
operator|.
name|FormatMethod
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_comment
comment|/** Structured result of a staleness check. */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|StalenessCheckResult
specifier|public
specifier|abstract
class|class
name|StalenessCheckResult
block|{
DECL|method|notStale ()
specifier|public
specifier|static
name|StalenessCheckResult
name|notStale
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_StalenessCheckResult
argument_list|(
literal|false
argument_list|,
name|Optional
operator|.
name|empty
argument_list|()
argument_list|)
return|;
block|}
DECL|method|stale (String reason)
specifier|public
specifier|static
name|StalenessCheckResult
name|stale
parameter_list|(
name|String
name|reason
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_StalenessCheckResult
argument_list|(
literal|true
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
name|reason
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|FormatMethod
DECL|method|stale (String reason, Object... args)
specifier|public
specifier|static
name|StalenessCheckResult
name|stale
parameter_list|(
name|String
name|reason
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
block|{
return|return
name|stale
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|reason
argument_list|,
name|args
argument_list|)
argument_list|)
return|;
block|}
DECL|method|isStale ()
specifier|public
specifier|abstract
name|boolean
name|isStale
parameter_list|()
function_decl|;
DECL|method|reason ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|reason
parameter_list|()
function_decl|;
block|}
end_class

end_unit

