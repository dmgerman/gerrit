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
DECL|package|com.google.gerrit.pgm.init
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|init
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Stage
operator|.
name|PRODUCTION
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
name|pgm
operator|.
name|util
operator|.
name|ConsoleUI
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
name|server
operator|.
name|config
operator|.
name|SitePaths
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Binding
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Guice
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Injector
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Key
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|TypeLiteral
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|name
operator|.
name|Named
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|name
operator|.
name|Names
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_comment
comment|/** Initialize the {@code database} configuration section. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|InitDatabase
class|class
name|InitDatabase
implements|implements
name|InitStep
block|{
DECL|field|ui
specifier|private
specifier|final
name|ConsoleUI
name|ui
decl_stmt|;
DECL|field|site
specifier|private
specifier|final
name|SitePaths
name|site
decl_stmt|;
DECL|field|libraries
specifier|private
specifier|final
name|Libraries
name|libraries
decl_stmt|;
DECL|field|database
specifier|private
specifier|final
name|Section
name|database
decl_stmt|;
annotation|@
name|Inject
DECL|method|InitDatabase (final ConsoleUI ui, final SitePaths site, final Libraries libraries, final Section.Factory sections)
name|InitDatabase
parameter_list|(
specifier|final
name|ConsoleUI
name|ui
parameter_list|,
specifier|final
name|SitePaths
name|site
parameter_list|,
specifier|final
name|Libraries
name|libraries
parameter_list|,
specifier|final
name|Section
operator|.
name|Factory
name|sections
parameter_list|)
block|{
name|this
operator|.
name|ui
operator|=
name|ui
expr_stmt|;
name|this
operator|.
name|site
operator|=
name|site
expr_stmt|;
name|this
operator|.
name|libraries
operator|=
name|libraries
expr_stmt|;
name|this
operator|.
name|database
operator|=
name|sections
operator|.
name|get
argument_list|(
literal|"database"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|ui
operator|.
name|header
argument_list|(
literal|"SQL Database"
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|allowedValues
init|=
operator|new
name|TreeSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Injector
name|i
init|=
name|Guice
operator|.
name|createInjector
argument_list|(
name|PRODUCTION
argument_list|,
operator|new
name|DatabaseConfigModule
argument_list|(
name|site
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Binding
argument_list|<
name|DatabaseConfigInitializer
argument_list|>
argument_list|>
name|dbConfigBindings
init|=
name|i
operator|.
name|findBindingsByType
argument_list|(
operator|new
name|TypeLiteral
argument_list|<
name|DatabaseConfigInitializer
argument_list|>
argument_list|()
block|{}
argument_list|)
decl_stmt|;
for|for
control|(
name|Binding
argument_list|<
name|DatabaseConfigInitializer
argument_list|>
name|binding
range|:
name|dbConfigBindings
control|)
block|{
name|Annotation
name|annotation
init|=
name|binding
operator|.
name|getKey
argument_list|()
operator|.
name|getAnnotation
argument_list|()
decl_stmt|;
if|if
condition|(
name|annotation
operator|instanceof
name|Named
condition|)
block|{
name|allowedValues
operator|.
name|add
argument_list|(
operator|(
operator|(
name|Named
operator|)
name|annotation
operator|)
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|dbType
init|=
name|database
operator|.
name|select
argument_list|(
literal|"Database server type"
argument_list|,
literal|"type"
argument_list|,
literal|"h2"
argument_list|,
name|allowedValues
argument_list|)
decl_stmt|;
name|DatabaseConfigInitializer
name|dci
init|=
name|i
operator|.
name|getInstance
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|DatabaseConfigInitializer
operator|.
name|class
argument_list|,
name|Names
operator|.
name|named
argument_list|(
name|dbType
operator|.
name|toLowerCase
argument_list|()
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|dci
operator|instanceof
name|MySqlInitializer
condition|)
block|{
name|libraries
operator|.
name|mysqlDriver
operator|.
name|downloadRequired
argument_list|()
expr_stmt|;
block|}
name|dci
operator|.
name|initConfig
argument_list|(
name|database
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

