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
DECL|package|com.google.gerrit.server.securestore.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|securestore
operator|.
name|testing
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
name|server
operator|.
name|securestore
operator|.
name|SecureStore
import|;
end_import

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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_class
DECL|class|InMemorySecureStore
specifier|public
class|class
name|InMemorySecureStore
extends|extends
name|SecureStore
block|{
DECL|field|cfg
specifier|private
specifier|final
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|getList (String section, String subsection, String name)
specifier|public
name|String
index|[]
name|getList
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
name|cfg
operator|.
name|getStringList
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getListForPlugin ( String pluginName, String section, String subsection, String name)
specifier|public
name|String
index|[]
name|getListForPlugin
parameter_list|(
name|String
name|pluginName
parameter_list|,
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"not used by tests"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|setList (String section, String subsection, String name, List<String> values)
specifier|public
name|void
name|setList
parameter_list|(
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|,
name|String
name|name
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|)
block|{
name|cfg
operator|.
name|setStringList
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|,
name|values
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|unset (String section, String subsection, String name)
specifier|public
name|void
name|unset
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
name|cfg
operator|.
name|unset
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|list ()
specifier|public
name|Iterable
argument_list|<
name|EntryKey
argument_list|>
name|list
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"not used by tests"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|isOutdated ()
specifier|public
name|boolean
name|isOutdated
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"not used by tests"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|reload ()
specifier|public
name|void
name|reload
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"not used by tests"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

