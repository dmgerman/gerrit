begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|com.google.gerrit.server.update
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|update
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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
name|common
operator|.
name|TimeUtil
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
name|lifecycle
operator|.
name|LifecycleManager
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
name|client
operator|.
name|Account
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
name|client
operator|.
name|Project
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
name|server
operator|.
name|ReviewDb
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
name|CurrentUser
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
name|IdentifiedUser
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
name|account
operator|.
name|AccountManager
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
name|account
operator|.
name|AuthRequest
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
name|schema
operator|.
name|SchemaCreator
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
name|util
operator|.
name|RequestContext
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
name|util
operator|.
name|ThreadLocalRequestContext
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
name|testutil
operator|.
name|InMemoryDatabase
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
name|testutil
operator|.
name|InMemoryModule
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
name|testutil
operator|.
name|InMemoryRepositoryManager
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
name|Provider
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
name|util
operator|.
name|Providers
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
name|internal
operator|.
name|storage
operator|.
name|dfs
operator|.
name|InMemoryRepository
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
name|junit
operator|.
name|TestRepository
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
name|revwalk
operator|.
name|RevCommit
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
name|transport
operator|.
name|ReceiveCommand
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|BatchUpdateTest
specifier|public
class|class
name|BatchUpdateTest
block|{
DECL|field|accountManager
annotation|@
name|Inject
specifier|private
name|AccountManager
name|accountManager
decl_stmt|;
DECL|field|userFactory
annotation|@
name|Inject
specifier|private
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
decl_stmt|;
DECL|field|schemaFactory
annotation|@
name|Inject
specifier|private
name|InMemoryDatabase
name|schemaFactory
decl_stmt|;
DECL|field|repoManager
annotation|@
name|Inject
specifier|private
name|InMemoryRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|schemaCreator
annotation|@
name|Inject
specifier|private
name|SchemaCreator
name|schemaCreator
decl_stmt|;
DECL|field|requestContext
annotation|@
name|Inject
specifier|private
name|ThreadLocalRequestContext
name|requestContext
decl_stmt|;
DECL|field|batchUpdateFactory
annotation|@
name|Inject
specifier|private
name|BatchUpdate
operator|.
name|Factory
name|batchUpdateFactory
decl_stmt|;
DECL|field|lifecycle
specifier|private
name|LifecycleManager
name|lifecycle
decl_stmt|;
DECL|field|db
specifier|private
name|ReviewDb
name|db
decl_stmt|;
DECL|field|repo
specifier|private
name|TestRepository
argument_list|<
name|InMemoryRepository
argument_list|>
name|repo
decl_stmt|;
DECL|field|project
specifier|private
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|field|user
specifier|private
name|IdentifiedUser
name|user
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|Injector
name|injector
init|=
name|Guice
operator|.
name|createInjector
argument_list|(
operator|new
name|InMemoryModule
argument_list|()
argument_list|)
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|lifecycle
operator|=
operator|new
name|LifecycleManager
argument_list|()
expr_stmt|;
name|lifecycle
operator|.
name|add
argument_list|(
name|injector
argument_list|)
expr_stmt|;
name|lifecycle
operator|.
name|start
argument_list|()
expr_stmt|;
name|db
operator|=
name|schemaFactory
operator|.
name|open
argument_list|()
expr_stmt|;
name|schemaCreator
operator|.
name|create
argument_list|(
name|db
argument_list|)
expr_stmt|;
name|Account
operator|.
name|Id
name|userId
init|=
name|accountManager
operator|.
name|authenticate
argument_list|(
name|AuthRequest
operator|.
name|forUser
argument_list|(
literal|"user"
argument_list|)
argument_list|)
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
name|user
operator|=
name|userFactory
operator|.
name|create
argument_list|(
name|userId
argument_list|)
expr_stmt|;
name|project
operator|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|InMemoryRepository
name|inMemoryRepo
init|=
name|repoManager
operator|.
name|createRepository
argument_list|(
name|project
argument_list|)
decl_stmt|;
name|repo
operator|=
operator|new
name|TestRepository
argument_list|<>
argument_list|(
name|inMemoryRepo
argument_list|)
expr_stmt|;
name|requestContext
operator|.
name|setContext
argument_list|(
operator|new
name|RequestContext
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|CurrentUser
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
annotation|@
name|Override
specifier|public
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|getReviewDbProvider
parameter_list|()
block|{
return|return
name|Providers
operator|.
name|of
argument_list|(
name|db
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
if|if
condition|(
name|repo
operator|!=
literal|null
condition|)
block|{
name|repo
operator|.
name|getRepository
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|lifecycle
operator|!=
literal|null
condition|)
block|{
name|lifecycle
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|requestContext
operator|.
name|setContext
argument_list|(
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|db
operator|!=
literal|null
condition|)
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|InMemoryDatabase
operator|.
name|drop
argument_list|(
name|schemaFactory
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addRefUpdateFromFastForwardCommit ()
specifier|public
name|void
name|addRefUpdateFromFastForwardCommit
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|RevCommit
name|masterCommit
init|=
name|repo
operator|.
name|branch
argument_list|(
literal|"master"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
specifier|final
name|RevCommit
name|branchCommit
init|=
name|repo
operator|.
name|branch
argument_list|(
literal|"branch"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|parent
argument_list|(
name|masterCommit
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
try|try
init|(
name|BatchUpdate
name|bu
init|=
name|batchUpdateFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|project
argument_list|,
name|user
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
init|)
block|{
name|bu
operator|.
name|addRepoOnlyOp
argument_list|(
operator|new
name|RepoOnlyOp
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|updateRepo
parameter_list|(
name|RepoContext
name|ctx
parameter_list|)
throws|throws
name|Exception
block|{
name|ctx
operator|.
name|addRefUpdate
argument_list|(
operator|new
name|ReceiveCommand
argument_list|(
name|masterCommit
operator|.
name|getId
argument_list|()
argument_list|,
name|branchCommit
operator|.
name|getId
argument_list|()
argument_list|,
literal|"refs/heads/master"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|repo
operator|.
name|getRepository
argument_list|()
operator|.
name|exactRef
argument_list|(
literal|"refs/heads/master"
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|branchCommit
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

