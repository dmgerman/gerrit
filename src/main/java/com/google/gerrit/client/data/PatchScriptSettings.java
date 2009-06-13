begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|client
operator|.
name|reviewdb
operator|.
name|AccountGeneralPreferences
import|;
end_import

begin_class
DECL|class|PatchScriptSettings
specifier|public
class|class
name|PatchScriptSettings
block|{
DECL|enum|Whitespace
specifier|public
specifier|static
enum|enum
name|Whitespace
block|{
DECL|enumConstant|IGNORE_NONE
DECL|enumConstant|IGNORE_SPACE_AT_EOL
DECL|enumConstant|IGNORE_SPACE_CHANGE
DECL|enumConstant|IGNORE_ALL_SPACE
name|IGNORE_NONE
block|,
name|IGNORE_SPACE_AT_EOL
block|,
name|IGNORE_SPACE_CHANGE
block|,
name|IGNORE_ALL_SPACE
block|;   }
DECL|field|context
specifier|protected
name|int
name|context
decl_stmt|;
DECL|field|whitespace
specifier|protected
name|Whitespace
name|whitespace
decl_stmt|;
DECL|method|PatchScriptSettings ()
specifier|public
name|PatchScriptSettings
parameter_list|()
block|{
name|context
operator|=
name|AccountGeneralPreferences
operator|.
name|DEFAULT_CONTEXT
expr_stmt|;
name|whitespace
operator|=
name|Whitespace
operator|.
name|IGNORE_NONE
expr_stmt|;
block|}
DECL|method|PatchScriptSettings (final PatchScriptSettings s)
specifier|public
name|PatchScriptSettings
parameter_list|(
specifier|final
name|PatchScriptSettings
name|s
parameter_list|)
block|{
name|context
operator|=
name|s
operator|.
name|context
expr_stmt|;
name|whitespace
operator|=
name|s
operator|.
name|whitespace
expr_stmt|;
block|}
DECL|method|getContext ()
specifier|public
name|int
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
DECL|method|setContext (final int ctx)
specifier|public
name|void
name|setContext
parameter_list|(
specifier|final
name|int
name|ctx
parameter_list|)
block|{
assert|assert
literal|0
operator|<=
name|ctx
operator|||
name|ctx
operator|==
name|AccountGeneralPreferences
operator|.
name|WHOLE_FILE_CONTEXT
assert|;
name|context
operator|=
name|ctx
expr_stmt|;
block|}
DECL|method|getWhitespace ()
specifier|public
name|Whitespace
name|getWhitespace
parameter_list|()
block|{
return|return
name|whitespace
return|;
block|}
DECL|method|setWhitespace (final Whitespace ws)
specifier|public
name|void
name|setWhitespace
parameter_list|(
specifier|final
name|Whitespace
name|ws
parameter_list|)
block|{
assert|assert
name|ws
operator|!=
literal|null
assert|;
name|whitespace
operator|=
name|ws
expr_stmt|;
block|}
block|}
end_class

end_unit

