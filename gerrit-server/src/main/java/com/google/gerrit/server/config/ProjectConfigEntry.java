begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
name|gerrit
operator|.
name|extensions
operator|.
name|annotations
operator|.
name|ExtensionPoint
import|;
end_import

begin_class
annotation|@
name|ExtensionPoint
DECL|class|ProjectConfigEntry
specifier|public
class|class
name|ProjectConfigEntry
block|{
DECL|enum|Type
specifier|public
enum|enum
name|Type
block|{
DECL|enumConstant|STRING
DECL|enumConstant|INT
DECL|enumConstant|LONG
DECL|enumConstant|BOOLEAN
name|STRING
block|,
name|INT
block|,
name|LONG
block|,
name|BOOLEAN
block|}
DECL|field|displayName
specifier|private
specifier|final
name|String
name|displayName
decl_stmt|;
DECL|field|defaultValue
specifier|private
specifier|final
name|String
name|defaultValue
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|Type
name|type
decl_stmt|;
DECL|method|ProjectConfigEntry (String displayName, String defaultValue)
specifier|public
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
name|this
argument_list|(
name|displayName
argument_list|,
name|defaultValue
argument_list|,
name|Type
operator|.
name|STRING
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectConfigEntry (String displayName, int defaultValue)
specifier|public
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|int
name|defaultValue
parameter_list|)
block|{
name|this
argument_list|(
name|displayName
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|defaultValue
argument_list|)
argument_list|,
name|Type
operator|.
name|INT
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectConfigEntry (String displayName, long defaultValue)
specifier|public
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|long
name|defaultValue
parameter_list|)
block|{
name|this
argument_list|(
name|displayName
argument_list|,
name|Long
operator|.
name|toString
argument_list|(
name|defaultValue
argument_list|)
argument_list|,
name|Type
operator|.
name|LONG
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectConfigEntry (String displayName, boolean defaultValue)
specifier|public
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|boolean
name|defaultValue
parameter_list|)
block|{
name|this
argument_list|(
name|displayName
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|defaultValue
argument_list|)
argument_list|,
name|Type
operator|.
name|BOOLEAN
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectConfigEntry (String displayName, String defaultValue, Type type)
specifier|private
name|ProjectConfigEntry
parameter_list|(
name|String
name|displayName
parameter_list|,
name|String
name|defaultValue
parameter_list|,
name|Type
name|type
parameter_list|)
block|{
name|this
operator|.
name|displayName
operator|=
name|displayName
expr_stmt|;
name|this
operator|.
name|defaultValue
operator|=
name|defaultValue
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getDisplayName ()
specifier|public
name|String
name|getDisplayName
parameter_list|()
block|{
return|return
name|displayName
return|;
block|}
DECL|method|getDefaultValue ()
specifier|public
name|String
name|getDefaultValue
parameter_list|()
block|{
return|return
name|defaultValue
return|;
block|}
DECL|method|getType ()
specifier|public
name|Type
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
block|}
end_class

end_unit

